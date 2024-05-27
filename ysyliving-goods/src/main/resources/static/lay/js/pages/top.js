const lyTop = {
    template: "\
    <div class='nav-top' > \
     <shortcut/>\
        <!--头部--> \
        <div class='header' id='headApp'> \
            <div class='py-container'> \
                <div class='yui3-g Logo'> \
                    <div class='yui3-u Left logoArea'> \
                        <a class='logo-bd' title='Suron' href='/api/seckill/login/secIndex' target='_blank'></a> \
                    </div> \
                    <div class='yui3-u Center searchArea'> \
                        <div class='search'> \
                            <form action='' class='sui-form form-inline'> \
                                <!--searchAutoComplete--> \
                                <div class='input-append'> \
                                    <input type='text' id='autocomplete'  v-model='key' \
                                           class='input-error input-xxlarge' value='咖啡'/> \
                                    <button @click='search' class='sui-btn btn-xlarge btn-danger' type='button'>搜索</button> \
                                </div> \
                            </form> \
                        </div> \
                        <div class='hotwords'> \
                            <ul> \
                                <li class='f-item'>Suron首发</li> \
                                <li class='f-item'>亿元优惠</li> \
                                <li class='f-item'>9.9元团购</li> \
                                <li class='f-item'>每满99减30</li> \
                                <li class='f-item'>亿元优惠</li> \
                                <li class='f-item'>9.9元团购</li> \
                                <li class='f-item'>办公用品</li> \
                            </ul> \
                        </div> \
                    </div> \
                    <div class='yui3-u Right shopArea'> \
                        <div class='fr shopcar'> \
                            <div class='show-shopcar' id='shopcar'> \
                                <span class='car'></span> \
                                <a class='sui-btn btn-default btn-xlarge' href='cart.html' target='_blank'> \
                                    <span>我的购物车</span> \
                                    <i class='shopnum'>0</i> \
                                </a> \
                                <div class='clearfix shopcarlist' id='shopcarlist' style='display:none'> \
                                    <p>'啊哦，你的购物车还没有商品哦！'</p> \
                                    <p>'啊哦，你的购物车还没有商品哦！'</p> \
                                </div> \
                            </div> \
                        </div> \
                    </div> \
                </div> \
                <div class='yui3-g NavList'> \
                    <div class='yui3-u Left all-sort'> \
                        <h4>Suron精品</h4> \
                    </div> \
                    <div class='yui3-u Center navArea'> \
                        <ul class='nav'> \
                            <li class='f-item'>服装城</li> \
                            <li class='f-item'>美妆馆</li> \
                            <li class='f-item'>品优超市</li> \
                            <li class='f-item'>全球购</li> \
                            <li class='f-item'>闪购</li> \
                            <li class='f-item'>团购</li> \
                            <li class='f-item'>有趣</li> \
                            <li class='f-item'><a href='/api/seckill/goods/toList' target='_blank'>秒杀</a></li> \
                        </ul> \
                    </div> \
                    <div class='yui3-u Right'></div> \
                </div> \
            </div> \
        </div>\
       </div> \
      ",
    name:'ly-top',
    data() {
        return {
            key: "",
            query: location.search
        }
    },
    methods: {
        search() {
            const url = '/api/goods/goods/search/page';
            const data = { key: this.key };
            fetch(url, {
                method: 'POST', // 设置请求方法为 POST
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data) // 将请求数据转换为 JSON 字符串
            })
                .then(resp => {
                    // 保存分页结果
                    this.total = resp.data.total;
                    this.totalPage = resp.data.totalPage;
                    //    保存当前页商品
                    resp.data.items.forEach(goods => {
                        //把json处理成js对象
                        goods.skus = JSON.parse(goods.skus);
                        //    初始化被选中的sku
                        goods.selectedSku = goods.skus[0];
                    })
                    this.goodsList = resp.data.items;
                    //    获取过滤结果，形成过滤项
                }).catch(Error => {
                console.error("Failed to load data:", error);
            });
            // window.location = '/api/goods/goods/search/page?key=' + this.key;
        },
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return decodeURI(r[2]);
            }
            return null;
        }
    },
    created() {
        this.key = this.getUrlParam("key");
    },
    components: {
        shortcut:() => import('/api/goods/lay/js/pages/shortcut.js')
    }
}
export default lyTop;