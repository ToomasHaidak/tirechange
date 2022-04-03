import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/AddWorkshop.vue'
import Register from '../views/BookTime.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/update',
    name: 'update',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Update.vue')
    }
  },

  {
    path: '/book',
    name: 'book',
    component: Register
  }
]

const router = new VueRouter({
  routes
})

export default router
