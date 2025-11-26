<template>
  <nav class="navbar navbar-expand navbar-dark bg-dark mb-4">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/transactions">
        FinPlan
      </router-link>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0" v-if="isAuth">
          <li class="nav-item">
            <router-link class="nav-link" to="/transactions">Транзакции</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/accounts">Счета</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/categories">Категории</router-link>
          </li>
        </ul>

        <ul class="navbar-nav ms-auto">
          <li v-if="isAuth" class="nav-item d-flex align-items-center me-2 text-light">
            {{ currentUser?.email }}
          </li>
          <li v-if="isAuth" class="nav-item">
            <button class="btn btn-outline-light btn-sm" @click="onLogout">
              Выйти
            </button>
          </li>

          <template v-else>
            <li class="nav-item">
              <router-link class="nav-link" to="/login">Вход</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/register">Регистрация</router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '@/features/auth/composables/useAuth';

const router = useRouter();
const { token, currentUser, logout } = useAuth();

const isAuth = computed(() => !!token.value);

function onLogout() {
  logout();
  router.push('/login');
}
</script>
