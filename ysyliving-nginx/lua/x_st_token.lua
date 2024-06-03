---
--- Created by binghe.
--- 获取token
---

local x_st_token = {}

function x_st_token.get_token()
    --获取请求头里的参数
    local headers = ngx.req.get_headers()
    --获取access_token
    local token = headers["x-st-token"]
    if not token then
        token = ""
    end
    return token
end

return x_st_token
