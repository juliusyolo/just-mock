import {createRouter,createWebHistory} from "vue-router";
// 导入组件
import Main from '../pages/Main.vue';
import MockInfo from '../pages/MockInfo.vue';
import * as path from "path";
import VMInstanceList from "../pages/VMInstanceList.vue";
import TemplateConfiguration from "../pages/TemplateConfiguration.vue";

// 创建路由实例
export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main,
      children:[
        {
          path: 'vmInstances',
          name: 'vmInstances',
          component: VMInstanceList,
        },
        {
          path: 'template',
          name: 'template',
          component: TemplateConfiguration,
        },
        {
          path: '/',
          redirect:'/vmInstances'
        },
      ]
    },
    {
      path: '/mock/info/:pid',
      name: 'mockInfo',
      component: MockInfo,
    },
    {
      path: '/:pathMatch(.*)*',
      redirect:'/vmInstances'
    }
  ],
});
