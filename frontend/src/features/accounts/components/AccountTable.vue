<template>
  <div v-if="accounts.length">
    <table class="table">
      <thead>
      <tr>
        <th>#</th>
        <th>Name</th>
        <th>Sort</th>
        <th>Balance</th>
        <th>Currency</th>
        <th>Update</th>
        <th>Delete</th>
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
            Update
          </button>
        </td>
        <td>
          <button class="btn btn-danger mb-2" @click="emit('delete', acc)">
            Delete
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div v-else>
    <p>No accounts yet</p>
  </div>
</template>

<script setup>
const props = defineProps({
  accounts: { type: Array, required: true }
});

const emit = defineEmits(['update', 'delete']);
</script>
