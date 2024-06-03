const shortcut = {
    template: "\
    <div class='py-container'> \
        <div class='shortcut'> \
            <ul class='fl'> \
               <li class='f-item'>Suron商城欢迎您！</li> \
               <li class='f-item' v-if='user && user.nickname'>\
               尊敬的会员，<span style='color: red;'>{{user.nickname}}</span>\
               </li>\
               <li v-else class='f-item'> \
                   请<a href='javascript:void(0)' @click='gotoLogin'>登录</a>　 \
                   <span><a href='#' target='_blank'>注册未开放</a></span> \
               </li> \
           </ul> \
           <ul class='fr'> \
               <li class='f-item'>我的订单</li> \
               <li class='f-item space'></li> \
               <li class='f-item'><a href='#' target='_blank'>主页</a></li> \
               <li class='f-item space'></li> \
               <li class='f-item'>会员</li> \
               <li class='f-item space'></li> \
               <li class='f-item'>企业采购</li> \
               <li class='f-item space'></li> \
               <li class='f-item'>关注乐优</li> \
               <li class='f-item space'></li> \
               <li class='f-item' id='service'> \
                   <span>客户服务</span> \
                   <ul class='service'> \
                       <li><a href='#' target='_blank'>合作招商</a></li> \
                       <li><a href='#' target='_blank'>商家后台</a></li> \
                       <li><a href='#' target='_blank'>合作招商</a></li> \
                       <li><a href='#'>商家后台</a></li> \
                   </ul> \
               </li> \
               <li class='f-item space'></li> \
               <li class='f-item'>网站导航</li> \
           </ul> \
       </div> \
    </div>\
    ",
    name: "shortcut",
    data() {
        return {
            user: "13388584660"
        }
    },
    created() {
        ly.http.get("#")
            .then(resp => {
                this.user = resp.data;
            })
    },
    methods: {
        gotoLogin() {
            // window.location = "/login.html?returnUrl=" + window.location;
            window.location = "/api/seckill/login/toLogin"
        }
    }
}
export default shortcut;