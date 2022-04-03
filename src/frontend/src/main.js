import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios";
import VueAxios from "vue-axios";

import VueDatePicker from '@mathieustan/vue-datepicker';
import '@mathieustan/vue-datepicker/dist/vue-datepicker.min.css';
Vue.use(VueDatePicker);

import {DropDownListPlugin} from '@syncfusion/ej2-vue-dropdowns';
Vue.use(DropDownListPlugin);

import SmartTable from 'vuejs-smart-table'
Vue.use(SmartTable)

Vue.use(VueAxios, axios)
Vue.config.productionTip = false

new Vue({
  router,
  render: function (h) { return h(App) }
}).$mount('#app')
