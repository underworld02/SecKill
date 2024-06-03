import Vue from 'vue';
import App from './App.vue';

// 创建一个全局事件总线
Vue.prototype.$eventBus = new Vue();

// 创建Vue实例
new Vue({
    render: h => h(App),
}).$mount('#app');