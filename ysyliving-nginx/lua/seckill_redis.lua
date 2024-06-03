---
--- Created by binghe
--- 操作Redis Lua脚本
---

--定义redis操作的封装
local redis_basic = require "redis_basic"

--定义一个模块
local lredis={}

--定义一个方法，Redis集群增加数据、查询数据、删除数据
--增加数据
function lredis.set(key,val)
    local red = redis_basic:new()
    red:set(key,val)
    red:close()
end

--根据key查询数据
function lredis.get(key)
    local red = redis_basic:new()
    local value = red:get(key)
    red:close()
    --返回数据
    return value
end

--根据key删除数据
function lredis.del(key)
    local red = redis_basic:new()
    red:del(key)
    red:close()
end

return lredis