<template>
  <form @submit.prevent="onSubmit" style="padding: 0px 400px 0px 400px">
    <div class="mb-3" v-if="showEmail">
      <label class="form-label">Email</label>
      <input v-model="email" type="email" class="form-control" required />
    </div>
    <div class="mb-3">
      <label class="form-label">Код из письма</label>
      <input v-model="code" type="text" class="form-control" required />
    </div>

    <p v-if="error" class="text-danger">{{ error }}</p>
    <p v-if="success" class="text-success">
      Email подтверждён. Теперь вы можете <router-link to="/login">войти</router-link>.
    </p>

    <div class="d-flex gap-2">
      <button class="btn btn-primary" type="submit" :disabled="loading">
        Подтвердить
      </button>
      <button class="btn btn-outline-secondary" type="button" @click="onResend" :disabled="resendLoading">
        Отправить код ещё раз
      </button>
    </div>

    <p v-if="resendMessage" class="mt-2" :class="resendError ? 'text-danger' : 'text-muted'">
      {{ resendMessage }}
    </p>
  </form>
</template>

<script setup>
import {nextTick, ref, watch} from 'vue';
import { useRoute } from 'vue-router';
import { useAuth } from '@/features/auth/composables/useAuth';

const route = useRoute();

const props = defineProps({
  email: String,
  showEmail: {type: Boolean, default: true}
});

const { confirmEmail, resendCode } = useAuth();
const email = ref(props.email ?? route.query.email ?? '');
const code = ref('');
const error = ref('');
const success = ref(false);
const loading = ref(false);
const showEmail = ref(props.showEmail ?? true);

const resendLoading = ref(false);
const resendMessage = ref('');
const resendError = ref(false);

const emit = defineEmits(['onSuccess']);

async function onSubmit() {
  error.value = '';
  success.value = false;
  loading.value = true;
  resendLoading.value = false;

  try {
    await confirmEmail(email.value, code.value);
    success.value = true;
    emit('onSuccess');
  } catch (e) {
    console.error(e);
    error.value = e.response?.data?.message || 'Ошибка подтверждения';
  } finally {
    loading.value = false;
  }
}

async function onResend() {
  resendMessage.value = '';
  resendError.value = false;
  resendLoading.value = true;

  try {
    await resendCode(email.value);
    resendMessage.value = 'Код повторно отправлен на указанный email.';
  } catch (e) {
    console.error(e);
    resendMessage.value = e.response?.data?.message || 'Не удалось отправить код';
    resendError.value = true;
  } finally {
    resendLoading.value = false;
  }
}
</script>
