server {
    listen       11111;
    server_name  localhost;

    error_log logs/domain-error.log error;
    access_log logs/domain-access.log access;
    charset utf-8;

    #lua_need_request_body on;

    #模拟用户id
    set_by_lua_block $user_id{
        return "binghe";
    }

    #用户token,可作为标识用户的唯一id
    set_by_lua_file $user_access_token D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/set_common_var.lua;


    #token编排
    set $token "";

    location / {
        root   html;
        index  index.html index.htm;
    }

    #location ^~ /seckill {
    #    limit_req zone=limit_by_user burst=1 nodelay;
    #    proxy_pass http://real_server;
    #    proxy_set_header Host $host;
    #    proxy_set_header X-Real-IP $remote_addr;
    #    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    #}

    #测试获取请求参数
    location = /test {
         content_by_lua_block {
             ngx.req.read_body()
             local args, err = ngx.req.get_post_args()

             if err == "truncated" then
                 -- one can choose to ignore or reject the current request here
             end

             if not args then
                 ngx.say("failed to get post args: ", err)
                 return
             end
             for key, val in pairs(args) do
                 if type(val) == "table" then
                     ngx.say(key, ": ", table.concat(val, ", "))
                 else
                     ngx.say(key, ": ", val)
                 end
             end
         }
     }

     #测试2
     location = /test2 {
          content_by_lua_block {
              ngx.req.read_body()
              local args, err = ngx.req.get_body_data()

              if err == "truncated" then
                  -- one can choose to ignore or reject the current request here
              end
              ngx.say(args)

          }
      }

    location /seckill-user/user/login {
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location /seckill-user/user/get {
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        header_filter_by_lua_block{
           local accessToken = require("access_token")
           local access_token = accessToken.get_access_token()
           ngx.header["x-st-token"] = ngx.md5(access_token.."1")
           --这里为了解决跨域问题设置的，不存在跨域时不需要设置以下header
           ngx.header["Access-Control-Allow-Headers"] = "x-st-token"
           ngx.header["Access-Control-Allow-Origin"] = "http://localhost:11111"
           ngx.header["Access-Control-Allow-Credentials"] = "true"
        }
    }

    #秒杀活动列表
    location /seckill-activity/activity/seckillActivityList {
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        access_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/get_activity_list_access.lua;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        #limit_req zone=limit_by_user_access_token burst=1 nodelay;
        #content_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/query_activity_list.lua;
        #设置返回的header，并将security token放在header中
        header_filter_by_lua_block{
             local accessToken = require("access_token")
             local access_token = accessToken.get_access_token()
             ngx.header["x-st-token"] = ngx.md5(access_token.."1")
             --这里为了解决跨域问题设置的，不存在跨域时不需要设置以下header
             ngx.header["Access-Control-Allow-Headers"] = "x-st-token"
             ngx.header["Access-Control-Allow-Origin"] = "http://localhost:11111"
             ngx.header["Access-Control-Allow-Credentials"] = "true"
        }
    }

    #秒杀活动详情
    location /seckill-activity/activity/seckillActivity {
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        access_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/get_activity_detail_access.lua;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        header_filter_by_lua_block{
             local accessToken = require("access_token")
             local access_token = accessToken.get_access_token()
             ngx.header["x-st-token"] = ngx.md5(access_token.."2")
             --这里为了解决跨域问题设置的，不存在跨域时不需要设置以下header
             ngx.header["Access-Control-Allow-Headers"] = "x-st-token"
             ngx.header["Access-Control-Allow-Origin"] = "http://localhost:11111"
             ngx.header["Access-Control-Allow-Credentials"] = "true"
        }
    }

    #秒杀商品列表
    location /seckill-goods/goods/getSeckillGoodsList {
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        access_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/get_goods_list_access.lua;

        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        header_filter_by_lua_block{
             local accessToken = require("access_token")
             local access_token = accessToken.get_access_token()
             ngx.header["x-st-token"] = ngx.md5(access_token.."3")
             --这里为了解决跨域问题设置的，不存在跨域时不需要设置以下header
             ngx.header["Access-Control-Allow-Headers"] = "x-st-token"
             ngx.header["Access-Control-Allow-Origin"] = "http://localhost:11111"
             ngx.header["Access-Control-Allow-Credentials"] = "true"
        }
    }

    #秒杀商品详情
    location /seckill-goods/goods/getSeckillGoods {
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        access_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/get_goods_detail_access.lua;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        header_filter_by_lua_block{
             local accessToken = require("access_token")
             local access_token = accessToken.get_access_token()
             ngx.header["x-st-token"] = ngx.md5(access_token.."4")
             --这里为了解决跨域问题设置的，不存在跨域时不需要设置以下header
             ngx.header["Access-Control-Allow-Headers"] = "x-st-token"
             ngx.header["Access-Control-Allow-Origin"] = "http://localhost:11111"
             ngx.header["Access-Control-Allow-Credentials"] = "true"
        }
    }

    #秒杀下单
    location /seckill-order/order/saveSeckillOrder {
        access_by_lua_file D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/lua/submit_access.lua;
        limit_req zone=limit_by_user_access_token burst=1 nodelay;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        error_page 500 502 503 504 @json_fail;
    }



    error_page   500 502 503 504  /503_fail.html;
    location = /50x.html {
        root   html;
    }

    include D:/Workspaces/myself/seckill/myself/seckill/seckill/seckill-nginx/domain/public.com;
}

server {
    listen       11112;
    server_name  localhost;

    location /hello {
        default_type text/plain;
        content_by_lua_block {
            ngx.say("{\"response\" : \"hello world!!!\"}")
        }
    }
}