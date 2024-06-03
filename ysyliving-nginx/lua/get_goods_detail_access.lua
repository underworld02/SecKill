---
--- Created by binghe.
--- 秒杀商品详情的校验
---
---
local tokenUtil = require("x_st_token")
local token = tokenUtil.get_token()

local accessToken = require("access_token")
local access_token = accessToken.get_access_token()

--校验用户是否登录
if not access_token or access_token == nil then
    ngx.say("{\"code\": 2012, \"data\" : \"用户未登录\"}")
    return ngx.exit(200)
end

--校验Token编排
local nativeToken = ngx.md5(access_token.."3")
if nativeToken ~= token then
    ngx.say("{\"code\": 2100, \"data\" : \"鉴权失败,您没有权限直接访问获取秒杀商品详情接口\"}")
    return ngx.exit(500)
end

--直接从Redis读取数据
local rredis = require("seckill_redis")
local json = require("cjson")
local number_util = require("number_util")
ngx. req.read_body()
local args = ngx.req.get_post_args()
local id = args["id"]
local version = args["version"]
local redisKey = "SECKILL_GOODS_CACHE_KEY_" .. id
--ngx.log(ngx.ERR, "redisKey:" .. redisKey)
local redisValue = rredis.get(redisKey)
if redisValue ~= nil and redisValue ~= ngx.null  then
    local jsonRedisObj = json.decode(redisValue)
    local dataResult = json.decode(jsonRedisObj)
    local dataVersion = dataResult.version
    local data = dataResult.data
    if data ~= nil and data ~= ngx.null then
        data["version"] = dataVersion
        local startTime = data["startTime"]
        local endTime = data["endTime"]
        local startTimeStr = os.date("%Y-%m-%d %H:%M:%S",  startTime / 1000)
        local endTimeStr = os.date("%Y-%m-%d %H:%M:%S",  endTime / 1000)
        data["startTime"] = startTimeStr
        data["endTime"] = endTimeStr
        data["id"] = number_util.LongNumber2String(data["id"])
        local imgUrl = data["imgUrl"]
        data["imgUrl"] = string.gsub(imgUrl, "\\" , "")
        data["inSeckilling"] = nil
        data["online"] = nil
        local result = {}
        result["code"] = 1001
        result["data"] = data
        ngx.say(json.encode(result))
        return ngx.exit(200)
    end
end