import { http } from './http';
import { unwrapSuccess } from './helpers';

export async function listCategories(params = {}) {
    const res = await http.get('/categories', { params });
    return unwrapSuccess(res);
}

export async function createCategory(payload) {
    const res = await http.post('/categories', payload); // RESTful: множественное имя
    return unwrapSuccess(res);
}

export async function updateCategory(id, payload) {
    const res = await http.put(`/categories/${id}`, payload);
    return unwrapSuccess(res);
}

export async function deleteCategory(id) {
    return http.delete(`/categories/${id}`);
}

export async function clearCategories() {
    return http.delete('/categories');
}
