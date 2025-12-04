<template>
  <div v-if="accounts.length">
    <table class="table">
      <thead>
      <tr>
        <th>#</th>
        <th>Имя</th>
        <th>Сортировка</th>
        <th>Баланс</th>
        <th>Валюта</th>
        <th>Изменить</th>
        <th>Удалить</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="acc in accounts" :key="acc.id">
        <th scope="row">{{ acc.id }}</th>
        <td>
          <span>{{ acc.name }}</span>
        </td>
        <td>
          <span>{{ acc.sort }}</span>
        </td>
        <td>
          <span>{{ acc.balance }}</span>
        </td>
        <td>
          <span>{{ acc.currency }}</span>
        </td>
        <td>
          <button class="btn btn-primary mb-2" @click="emit('update', acc)">
            Изменить
          </button>
        </td>
        <td>
          <button class="btn btn-danger mb-2" @click="emit('delete', acc)">
            Удалить
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div v-else>
    <p>Нет счетов</p>
  </div>
</template>

<script setup>
const props = defineProps({
  accounts: { type: Array, required: true }
});

const emit = defineEmits(['update', 'delete']);
</script>
