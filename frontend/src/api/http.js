import axios from 'axios';
import router from "@/router";

const http = axios.create({
    baseURL: '/api',
    timeout: 10000,
    withCredentials: false,
});

export async function checkAuth() {
    try {
        await http.get('/auth/valid');
        return true;
    } catch (error) {
        console.log(error);
        if (error?.status && error?.status === 401 || error?.status === 403) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        }
        return false;
    }
}

http.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    console.log(token);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export { http };

class ApiError extends Error {
    constructor({ status, title, message, code, details }) {
        super(message);
        this.status = status;
        this.title = title;
        this.code = code;
        this.details = details;
    }
}

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

http.interceptors.response.use(
    (response) => response.data,
    (error) => {
        error = normalizeError(error);
    
        if (error.status === 401 || error.status == 403) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            router.push("/login");
        }
    
        return Promise.reject(new ApiError(error));
    }
);
