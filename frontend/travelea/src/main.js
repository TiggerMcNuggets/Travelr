import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'

import Datetime from 'vue-datetime'
// You need a specific loader for CSS files
import 'vue-datetime/dist/vue-datetime.css'


Vue.config.productionTip = false

Vue.use(Datetime)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
