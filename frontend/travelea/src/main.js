import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'

// You need a specific loader for CSS files


Vue.config.productionTip = false


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
