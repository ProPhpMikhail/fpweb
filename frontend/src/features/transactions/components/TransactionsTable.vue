<template>
  <div v-if="transactions.length">
    <table class="table">
      <thead>
      <tr>
        <th>#</th>
        <th>Имя</th>
        <th>Счет</th>
        <th>Сумма</th>
        <th>Категория</th>
        <th>Дата</th>
        <th>Гео</th>
        <th>Изменить</th>
        <th>Удалить</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="t in transactions" :key="t.id">
        <th scope="row">{{ t.id }}</th>
        <td>{{ t.name }}</td>
        <td>{{ t.accountName }}</td>
        <td>{{ t.amount < 0 ? t.amount : '+' + t.amount }}</td>
        <td>{{ t.categoryName }}</td>
        <td>{{ formatLocal(t.createdAt) }}</td>
        <td>
          <button
              class="btn"
              :hidden="!t.latitude || !t.longitude"
              :disabled="!t.latitude || !t.longitude"
              @click="emit('openMap', {
                latitude: t.latitude,
                longitude: t.longitude
              }, false)"
          >
            📍 Geo
          </button>
        </td>
        <td>
          <button class="btn btn-primary" @click="emit('update', t)">Изменить</button>
        </td>
        <td>
          <button class="btn btn-danger" @click="emit('delete', t)">Удалить</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div v-else class="muted">
    <p>Нет транзакций</p>
  </div>
</template>

<script setup>
import { formatLocal } from '@/api/helpers';

const props = defineProps({
  transactions: { type: Array, required: true },
});
const emit = defineEmits(['update, delete', 'openMap']);
</script>
