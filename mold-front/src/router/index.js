import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // 在这里添加一个后端管理界面的路配由置
        {
            path:"/",
            name:"home",
            component:()=>import("../views/Home.vue"),
            children:[
                {
                    path:"/client",
                    name:"client",
                    component:() => import("../views/client/index.vue")
                },
                {
                    path:"/orders",
                    name:"orders",
                    component:() => import("../views/orders/index.vue")
                },
                {
                    path:"/payment",
                    name:"payment",
                    component:() => import("../views/payment/index.vue")
                }
            ]
        }
    ],
})
export default router