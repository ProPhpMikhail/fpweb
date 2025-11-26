<template>
  <div id="app" style="text-align: center">
    <Navbar />
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { RouterView, RouterLink } from 'vue-router';
import styles from './assets/ui.css';
import Navbar from "@/layouts/Navbar.vue";
</script>

<style>
#app {
  font-family: Arial, sans-serif;
}

/* примитивное меню */
.navbar {
  display: flex;
  gap: 16px;
  padding: 12px 24px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 16px;
}

.nav-link {
  text-decoration: none;
  color: #007bff;
}

.nav-link.router-link-active {
  font-weight: bold;
  text-decoration: underline;
}

.content {
  padding: 0 24px;
}
</style>




