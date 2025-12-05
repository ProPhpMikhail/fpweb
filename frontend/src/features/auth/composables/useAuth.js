import { ref } from 'vue';
import { login as apiLogin, register as apiRegister, confirmEmail as apiConfirmEmail, resendCode as apiResendCode } from '@/api/auth';

const token = ref(localStorage.getItem('token'));
const currentUser = ref(
    token.value
        ? JSON.parse(localStorage.getItem('user') || 'null')
        : null
);

export function useAuth() {
    async function login(email, password) {
        const { data } = await apiLogin(email, password);
        token.value = data.token;
        currentUser.value = {
            email: data.email,
            role: data.role,
        };
        
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(currentUser.value));
        console.log(localStorage.getItem('token'))
    }
    
    async function register(email, password) {
        const { data } = await apiRegister(email, password);
    }
    
    async function confirmEmail(email, code) {
        const { data } = await apiConfirmEmail(email, code);
    }
    
    async function resendCode(email, code) {
        const { data } = await apiResendCode(email, code);
    }
    
    function logout() {
        token.value = null;
        currentUser.value = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
    
    return {
        token,
        currentUser,
        resendCode,
        login,
        register,
        confirmEmail,
        logout,
    };
}
