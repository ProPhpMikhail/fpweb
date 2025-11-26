import { http } from './http';

export function register(email, password) {
    return http.post('/auth/register', { email, password });
}

export function confirmEmail(email, code) {
    return http.post('/auth/confirm', { email, code });
}

export function login(email, password) {
    return http.post('/auth/login', { email, password });
}

export function resendCode(email) {
    return http.post('/auth/resend', { email });
}
