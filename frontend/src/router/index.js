import { createRouter, createWebHistory } from 'vue-router';
import TransactionsPage from '@/features/transactions/pages/TransactionsPage.vue';
import AccountsPage from '@/features/accounts/pages/AccountsPage.vue';
import CategoriesPage from "@/features/categories/pages/CategoriesPage.vue";

const routes = [
    {
        path: '/',
        redirect: '/transactions',
    },
    {
        path: '/transactions',
        name: 'Transactions',
        component: TransactionsPage,
    },
    {
        path: '/categories',
        name: 'Categories',
        component: CategoriesPage,
    },
    {
        path: '/accounts',
        name: 'Accounts',
        component: AccountsPage,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
