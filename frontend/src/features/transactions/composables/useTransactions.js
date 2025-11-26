import {ref, watch} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
    listTransactions,
    createTransaction,
    updateTransaction,
    deleteTransaction,
    clearTransactions,
} from '@/api/transactions';
import {
    listAccounts,
} from '@/api/accounts';
import {
    listCategories,
} from '@/api/categories';


export function useTransactions() {
    const route = useRoute();
    const router = useRouter();
    const transactions = ref([]);
    const accounts = ref([]);
    const categories = ref([]);
    const selectAccountId = ref('');
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
    
    async function load(pageArg = 1, sizeArg = 10) {
        loading.value = true;
        error.value = null;
        try {
            const accountData = await listAccounts();
            accounts.value = accountData.content;
            const categoriesData = await listCategories();
            categories.value = categoriesData.content;
            if (!selectAccountId.value && accounts.value.length) {
                selectAccountId.value = accounts.value[0].id;
            }
            if (accounts.value.length) {
                const data = await listTransactions(pageArg, sizeArg);
                transactions.value = data.content || [];
                page.value = data.number + 1;
                size.value = data.size;
                totalPages.value = data.totalPages;
                totalElements.value = data.totalElements;
            }
        } catch (e) {
            console.error(e);
            error.value = e;
            showErr('Не удалось загрузить');
        } finally {
            loading.value = false;
        }
    }
    
    async function addTransaction(payload) {
        try {
            await createTransaction(payload);
            showOk('Транзакция добавлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось добавить транзакцию');
        }
    }
    
    async function updateTransactionById(id, payload) {
        try {
            await updateTransaction(id, payload);
            showOk('Транзакция обновлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось обновить транзакцию');
        }
    }
    
    async function deleteTransactionById(id) {
        try {
            await deleteTransaction(id);
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
        transactions,
        accounts,
        categories,
        loading,
        error,
        message,
        messageOk,
        page,
        size,
        totalPages,
        totalElements,
        load,
        addTransaction,
        updateTransactionById,
        deleteTransactionById,
        clearAll,
    };
}
