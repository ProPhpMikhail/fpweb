import { ref, computed } from 'vue';
import {
    listAccounts,
    createAccount,
    updateAccount,
    deleteAccount,
    clearAccount,
} from '@/api/accounts';


export function useAccounts() {
    const accounts = ref([]);
    const currencies = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const message = ref('');
    const messageOk = ref(null);
    
    function showOk(msg) {
        message.value = msg;
        messageOk.value = true;
    }
    
    function showErr(msg) {
        message.value = msg;
        messageOk.value = false;
    }
    
    async function load() {
        loading.value = true;
        error.value = null;
        try {
            const data = await listAccounts();
            accounts.value = data.content;
            currencies.value = [
                {
                    id: 'RUB',
                    name: 'RUB',
                },
                {
                    id: 'USD',
                    name: 'USD',
                }
            ];
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
        load,
        addAccount,
        updateAccountById,
        deleteAccountById,
        clearAll,
    };
}
