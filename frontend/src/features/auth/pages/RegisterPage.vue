<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Register</h1>
        <p class="page-subtitle">Регистрация</p>
      </div>
    </header>
    <div v-if="successFinish" style="padding: 0px 400px 0px 400px">
      Email подтверждён. Теперь вы можете <router-link to="/login">войти</router-link>.
    </div>
    <form @submit.prevent="onSubmitReg" v-if="regForm" style="padding: 0px 400px 0px 400px">
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
      <p v-if="error" v-html="error" class="text-danger"></p>
      <p v-if="success" class="text-success">
        Регистрация успешна. Проверьте почту — мы отправили код подтверждения.
      </p>
      <button class="btn btn-primary" type="submit" :disabled="loading">
        Зарегистрироваться
      </button>
    </form>

    <ConfirmForm v-if="confirmForm"
        :email="email"
        :show-email="false"
         @onSuccess="onSuccess"
    />
  </div>
</template>

<script setup>
import {nextTick, ref, watch} from 'vue';
import { useAuth } from '@/features/auth/composables/useAuth';
import { useRouter } from 'vue-router';
import ConfirmForm from "@/features/auth/components/ConfirmForm.vue";

const email = ref('');
const password = ref('');
const password2 = ref('');
const regForm = ref(true);
const confirmForm = ref(false);
const error = ref('');
const success = ref(false);
const successFinish = ref(false);
const loading = ref(false);
const { register } = useAuth();
const router = useRouter();

function showConfirm() {
  regForm.value = false;
  confirmForm.value = true;
}

async function onSubmitReg() {
  error.value = '';
  success.value = false;

  if (password.value !== password2.value) {
    error.value = 'Пароли не совпадают';
    return;
  }

  loading.value = true;
  try {
    await register(email.value, password.value);
    regForm.value = false;
    confirmForm.value = true;
  } catch (e) {
    console.error(e.code);
    switch (e.code) {
      case 'email_not_confirmed':
        error.value = 'Этот email уже зарегистрирован, но не подтвержден, можете его <a href="#" id="confirm-link">подтвердить</a>';
        break;
      case 'email_exists':
        error.value = 'Этот email уже зарегистрирован';
        break;
      default:
        if (e.details) {
          e.details.forEach(function (err)  {
            if (err.code === 'invalid_password') {
              error.value = 'Пароль должен содержать мин одну цифру, одну букву и иметь длину более 8 символов';
            }
          });
        } else {
          error.value = 'Ошибка регистрации';
        }
        break;
    }
  } finally {
    loading.value = false;
  }
}

function onSuccess() {
  regForm.value = false;
  confirmForm.value = false;
  successFinish.value = true;
}

watch(error, async () => {
  await nextTick();
  const link = document.querySelector("#confirm-link");
  if (link) {
    link.addEventListener("click", showConfirm);
  }
});

</script>
