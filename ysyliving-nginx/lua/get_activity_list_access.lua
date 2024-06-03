---
--- Created by binghe.
--- 获取秒杀活动列表的校验
---

--校验用户是否登录
local accessToken = require("access_token")
local access_token = accessToken.get_access_token()
if not access_token then
    ngx.say("{\"code\": 2012, \"data\" : \"用户未登录\"}")
    return ngx.exit(200)
end

--直接从Redis读取数据
local rredis = require("seckill_redis")
local json = require("cjson")
local number_util = require("number_util")
ngx. req.read_body()
local args = ngx.req.get_post_args()
local status = args["status"]
local version = args["version"]
local redisKey = "SECKILL_ACTIVITIES_CACHE_KEY_" .. status
--ngx.log(ngx.ERR, "redisKey:" .. redisKey)
local redisValue = rredis.get(redisKey)
--ngx.log(ngx.ERR, "redisValue:" .. redisValue)
if redisValue ~= nil and redisValue ~= ngx.null  then
    local jsonRedisObj = json.decode(redisValue)
    local dataResult = json.decode(jsonRedisObj)
    local dataVersion = dataResult.version
    local data = dataResult.data
    if data ~= nil and data ~= ngx.null then
        local transferData = {}
        for i, v in pairs(data) do
            v["version"] = dataVersion
            local startTime = v["startTime"]
            local endTime = v["endTime"]
            local startTimeStr = os.date("%Y-%m-%d %H:%M:%S",  startTime / 1000)
            local endTimeStr = os.date("%Y-%m-%d %H:%M:%S",  endTime / 1000)
            v["startTime"] = startTimeStr
            v["endTime"] = endTimeStr
            v["inSeckilling"] = nil
            v["online"] = nil
            --local id = v["id"]
            v["id"] = number_util.LongNumber2String(v["id"])
            transferData[i] = v
        end
        local result = {}
        result["code"] = 1001
        result["data"] = transferData
        ngx.say(json.encode(result))
        return ngx.exit(200)
    end
end