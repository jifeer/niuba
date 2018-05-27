import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/views/Home';
import DTCake from '@/views/dt-cake';
import CHTCake from '@/views/cht-cake';
import HXCCake from '@/views/hxc-cake';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/dt-cake',
      name: 'DTCake',
      component: DTCake
    },
    {
      path: '/cht-cake',
      name: 'CHTCake',
      component: CHTCake
    },
    {
      path: '/hxc-cake',
      name: 'HXCCake',
      component: HXCCake
    }
  ]
})
