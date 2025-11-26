import { ref, computed } from 'vue';
import {
    listCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    clearCategories,
} from '@/api/categories';


export function useCategories() {
    const categories = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const message = ref('');
    const messageOk = ref(null); // true/false/null
    
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
            const data = await listCategories();
            categories.value = data.content;
            showOk('Данные загружены');
        } catch (e) {
            console.error(e);
            error.value = e;
            showErr('Не удалось загрузить');
        } finally {
            loading.value = false;
        }
    }
    
    async function addCategory(payload) {
        try {
            await createCategory(payload);
            showOk('Категория добавлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось добавить категорию');
        }
    }
    
    async function updateCategoryById(id, payload) {
        try {
            await updateCategory(id, payload);
            showOk('Категория обновлена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось обновить категорию');
        }
    }
    
    async function deleteCategoryById(id) {
        try {
            await deleteCategory(id);
            showOk('Категория удалена');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось удалить категорию');
        }
    }
    
    async function clearAll() {
        try {
            await clearCategories();
            showOk('Все категории удалены');
            await load();
        } catch (e) {
            console.error(e);
            showErr('Не удалось очистить категории');
        }
    }
    
    return {
        categories,
        loading,
        error,
        message,
        messageOk,
        load,
        addCategory,
        updateCategoryById,
        deleteCategoryById,
        clearAll,
    };
}
