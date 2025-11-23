import { http } from './http';
import { unwrapSuccess } from './helpers';

export async function listAccounts(params = {}) {
    const res = await http.get('/accounts', { params });
    return unwrapSuccess(res);
}

export async function createAccount(payload) {
    const res = await http.post('/accounts', payload); // RESTful: множественное имя
    return unwrapSuccess(res);
}

export async function updateAccount(id, payload) {
    const res = await http.put(`/accounts/${id}`, payload);
    return unwrapSuccess(res);
}

export async function deleteAccount(id) {
    // 204 No Content — вернётся пусто; unwrap просто вернёт undefined
    return http.delete(`/accounts/${id}`);
}

export async function clearAccount() {
    return http.delete('/accounts');
}
