<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Login</h1>
        <p class="page-subtitle">Авторизация</p>
      </div>
    </header>
    <form @submit.prevent="onSubmit" class="mt-3"  style="padding: 0px 400px 0px 400px">
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input v-model="email" type="email" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Пароль</label>
        <input v-model="password" type="password" class="form-control" required />
      </div>
      <p v-if="error" class="text-danger" v-html="error"></p>
      <button class="btn btn-primary" type="submit" :disabled="loading">
        Войти
      </button>

      <p class="mt-3">
        Нет аккаунта?
        <router-link to="/register">Регистрация</router-link>
      </p>
      <p class="mt-1">
        Не подтвердили email?
        <router-link to="/confirm">Подтвердить</router-link>
      </p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuth } from '@/features/auth/composables/useAuth';

const router = useRouter();
const route = useRoute();
const { login } = useAuth();

const email = ref('');
const password = ref('');
const error = ref('');
const loading = ref(false);

async function onSubmit() {
  error.value = '';
  loading.value = true;
  try {
    await login(email.value, password.value);
    const redirect = route.query.redirect || '/transactions';
    router.push(redirect);
  } catch (e) {
    console.error(e);
    switch (e.code) {
      case 'email_not_confirmed':
        error.value = 'Этот email не подтвержден, можете его <a href="/confirm?email=' + email.value + '">подтвердить</a>';
        break;
      default:
        error.value = 'Ошибка входа. Проверьте email/пароль';
        break;
    }
  } finally {
    loading.value = false;
  }
}
</script>
