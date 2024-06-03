---
--- Created by binghe.
--- 获取AccessToken
---

local access_token = {}

function access_token.get_access_token()
    --获取请求头里的参数
    local headers = ngx.req.get_headers()
    --获取access_token
    local user_access_token = headers["access-token"]
    if not user_access_token then
        user_access_token = ""
    end
    return user_access_token
end

return access_token
