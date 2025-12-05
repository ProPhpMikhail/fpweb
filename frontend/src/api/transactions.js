import { http } from './http';
import { unwrapSuccess } from './helpers';

export async function listTransactions(page = 1, size = 20, filter = {}) {
    const res = await http.get('/transactions', {
        params: { page, size, ...filter },
    });
    return unwrapSuccess(res);
}

export async function createTransaction(payload) {
    const res = await http.post('/transactions', payload);
    return unwrapSuccess(res);
}

export async function updateTransaction(id, payload) {
    const res = await http.put(`/transactions/${id}`, payload);
    return unwrapSuccess(res);
}

export async function deleteTransaction(id) {
    // 204 No Content — вернётся пусто; unwrap просто вернёт undefined
    return http.delete(`/transactions/${id}`);
}

export async function clearTransactions() {
    return http.delete('/transactions');
}
