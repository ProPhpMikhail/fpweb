import { createRouter, createWebHistory } from 'vue-router';
import { checkAuth } from "@/api/http";

const routes = [
    {
        path: '/',
        redirect: '/transactions',
    },
    {
        path: '/transactions',
        name: 'Transactions',
        component: () => import("@/features/transactions/pages/TransactionsPage.vue"),
        meta: {
            authorized: true
        },
    },
    {
        path: '/categories',
        name: 'Categories',
        component: () => import("@/features/categories/pages/CategoriesPage.vue"),
        meta: {
            authorized: true
        },
    },
    {
        path: '/accounts',
        name: 'Accounts',
        component: () => import("@/features/accounts/pages/AccountsPage.vue"),
        meta: {
            authorized: true
        },
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/features/auth/pages/LoginPage.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/features/auth/pages/RegisterPage.vue')
    },
    {
        path: '/confirm',
        name: 'Confirm',
        component: () => import('@/features/auth/pages/ConfirmPage.vue')
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach(async (to, from, next) => {
    const authorized = to.meta.authorized === true;
    if (authorized) {
        console.log(to.fullPath);
        await checkAuth();
    }
    const token = localStorage.getItem('token');
    
    if (authorized && !token) {
        next({
            name: 'Login',
            query: { redirect: to.fullPath },
        });
    } else {
        next();
    }
});

export default router;
