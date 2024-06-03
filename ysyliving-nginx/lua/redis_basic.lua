---
--- Created by binghe.
--- 读取Redis基础Lua文件
---

local redis = require "resty.redis"

local config = {
    host = "127.0.0.1",
    port = 6379,
    --pass = "123456"  -- redis 密码，没有密码的话，把这行注释掉
    pass = nil  -- redis 密码，没有密码的话，把这行注释掉
}

local _M = {}

function _M.new(self)
    local red = redis:new()
    red:set_timeout(1000) -- 1 second
    local res = red:connect(config['host'], config['port'])
    ngx.log(ngx.INFO,"Redis的连接为::",res)
    if not res then
        return nil
    end
    if config['pass'] ~= nil then
        res = red:auth(config['pass'])
        if not res then
            return nil
        end
    end
    red.close = close
    return red
end

local function close(self)
    local sock = self.sock
    if not sock then
        return nil, "not initialized"
    end
    if self.subscribed then
        return nil, "subscribed state"
    end
    return sock:setkeepalive(10000, 50)
end

return _M
