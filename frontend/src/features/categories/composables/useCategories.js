import { ref, computed } from 'vue';
import {
    listCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    clearCategories,
} from '@/api/categories';
import {useRoute} from "vue-router";


export function useCategories() {
    const route = useRoute();
    const categories = ref([]);
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
            const data = await listCategories(pageArg, sizeArg);
            page.value = data.number + 1;
            size.value = data.size;
            totalPages.value = data.totalPages;
            totalElements.value = data.totalElements;
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
        page,
        size,
        totalPages,
        totalElements,
        load,
        addCategory,
        updateCategoryById,
        deleteCategoryById,
        clearAll,
    };
}
