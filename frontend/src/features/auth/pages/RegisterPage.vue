<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Register</h1>
        <p class="page-subtitle">Регистрация</p>
      </div>
    </header>
    <div>
    <form @submit.prevent="onSubmit" style="padding: 0px 400px 0px 400px">
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input v-model="email" type="email" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Пароль</label>
        <input v-model="password" type="password" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Повторите пароль</label>
        <input v-model="password2" type="password" class="form-control" required />
      </div>
      <p v-if="error" class="text-danger">{{ error }}</p>
      <p v-if="success" class="text-success">
        Регистрация успешна. Проверьте почту — мы отправили код подтверждения.
      </p>
      <button class="btn btn-primary" type="submit" :disabled="loading">
        Зарегистрироваться
      </button>
    </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuth } from '@/features/auth/composables/useAuth';
import { useRouter } from 'vue-router';

const email = ref('');
const password = ref('');
const password2 = ref('');
const error = ref('');
const success = ref(false);
const loading = ref(false);
const { register } = useAuth();
const router = useRouter();

async function onSubmit() {
  error.value = '';
  success.value = false;

  if (password.value !== password2.value) {
    error.value = 'Пароли не совпадают';
    return;
  }

  loading.value = true;
  try {
    await register(email.value, password.value);
    router.push({
      name: 'Confirm',
      query: { email: email.value },
    });
  } catch (e) {
    console.error(e);
    error.value = e.response?.data?.message || 'Ошибка регистрации';
  } finally {
    loading.value = false;
  }
}
</script>
