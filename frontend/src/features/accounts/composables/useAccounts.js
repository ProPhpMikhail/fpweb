import { ref, computed } from 'vue';
import {
    listAccounts,
    createAccount,
    updateAccount,
    deleteAccount,
    clearAccount, listCurrencies,
} from '@/api/accounts';
import {useRoute} from "vue-router";


export function useAccounts() {
    const route = useRoute();
    const accounts = ref([]);
    const currencies = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const message = ref('');
    const messageOk = ref(null);
    
    const page = ref( Number(route.query.page ?? 1) );
    const size = ref( Number(route.query.size ?? 3) );
    const totalPages = ref( Number(0) );
    const totalElements = ref( Number(0) );
    
    function showOk(msg) {
        message.value = msg;
        messageOk.value = true;
    }
    
    function showErr(msg) {
        message.value = msg;
        messageOk.value = false;
    }
    
    async function load(pageArg = 1, sizeArg = 20) {
        loading.value = true;
        error.value = null;
        try {
            const data = await listAccounts(pageArg, sizeArg);
            accounts.value = data.content;
            page.value = data.number + 1;
            size.value = data.size;
            totalPages.value = data.totalPages;
            totalElements.value = data.totalElements;
            currencies.value = await listCurrencies();
            showOk('Данные загружены');
        } catch (e) {
            console.error(e);
            error.value = e;
            showErr('Не удалось загрузить');
        } finally {
            loading.value = false;
        }
    }
    
    async function addAccount(payload) {
        try {
            await createAccount(payload);
            showOk('Транзакция добавлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось добавить транзакцию');
        }
    }
    
    async function updateAccountById(id, payload) {
        try {
            await updateAccount(id, payload);
            showOk('Транзакция обновлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось обновить транзакцию');
        }
    }
    
    async function deleteAccountById(id) {
        try {
            await deleteAccount(id);
            showOk('Транзакция удалена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось удалить транзакцию');
        }
    }
    
    async function clearAll() {
        try {
            await clearTransactions();
            showOk('Все транзакции удалены');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось очистить транзакции');
        }
    }
    
    return {
        accounts,
        loading,
        currencies,
        error,
        message,
        messageOk,
        page,
        size,
        totalPages,
        totalElements,
        load,
        addAccount,
        updateAccountById,
        deleteAccountById,
        clearAll,
    };
}
