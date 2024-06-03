#以"@"开头定义的location，是内部接口，外部无法访问
location @json_fail {
    default_type application/json;
    return 200 '{"code":"21001","data":"当前请求已被网关拦截"}';
}