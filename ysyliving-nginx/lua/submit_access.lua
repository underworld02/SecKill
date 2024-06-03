-- 抢购下单校验

-- 引入黑名单相关的缓存
local black_cache = require "black_user_cache"
local accessToken = require "access_token"
local tokenUtil = require "x_st_token"

-- 用户登录后的Token
local user_token = accessToken.get_access_token()

-- 前面接口返回的Token
local token = tokenUtil.get_token()

-- 统计key，就用user_token
local key = ngx.md5(user_token)

-- 校验是否在黑名单
local flag = black_cache.check(key)
if not flag then
    ngx.log(ngx.ERR,key.." 已拉黑")
    return ngx.exit(500)
end

-- 校验是否触碰黑名单生成规则
local ff = black_cache.filter(key)
if not ff then
    return ngx.exit(500)
end

--校验用户是否登录
if not user_token then
    ngx.say("{\"code\": 2012, \"data\" : \"用户未登录\"}")
    return ngx.exit(200)
end

-- 校验编排Token是否合法
local nativeToken = ngx.md5(user_token.."4")
if nativeToken ~= token then
    ngx.say("{\"code\": 2100, \"data\" : \"鉴权失败,您没有权限直接访问秒杀下单接口\"}")
    return ngx.exit(500)
end