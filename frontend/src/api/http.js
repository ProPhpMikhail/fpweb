import axios from 'axios';

const http = axios.create({
    baseURL: '/api',
    timeout: 10000,
    withCredentials: false,
});

http.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export { http };

function normalizeError(error) {
    if (!error.response) {
        return {
            status: 0,
            title: 'Network error',
            message: 'Нет связи с сервером или таймаут',
            code: 'network_error',
            details: null,
            raw: error,
        };
    }
    
    const { status, data } = error.response;
    
    const title = data?.title || 'Request failed';
    const message = data?.detail || data?.message || 'Некорректный запрос';
    const code = data?.code || `http_${status}`;
    
    const details = data?.errors || null;
    
    return { status, title, message, code, details, raw: error };
}

export function unwrapSuccess(enveloped) {
    // ожидаем { success: true, data: ... }
    if (enveloped && enveloped.success) return enveloped.data;
    // если вдруг без конверта (например, GET отдаёт просто массив)
    return enveloped;
}

http.interceptors.response.use(
    (response) => response.data,
    (error) => Promise.reject(normalizeError(error))
);
