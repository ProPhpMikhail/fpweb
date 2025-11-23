<template>
  <div v-if="categories.length">
    <table class="table">
      <thead>
      <tr>
        <th>#</th>
        <th>Name</th>
        <th>Sort</th>
        <th>Update</th>
        <th>Delete</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cat in categories" :key="cat.id">
        <th scope="row">{{ cat.id }}</th>
        <td>
          <span>{{ cat.name }}</span>
        </td>
        <td>
          <span>{{ cat.sort }}</span>
        </td>
        <td>
          <button class="btn btn-primary mb-2" @click="emit('update', cat)">
            Update
          </button>
        </td>
        <td>
          <button class="btn btn-danger mb-2" @click="emit('delete', cat)">
            Delete
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <div v-else>
    <p>No categories yet</p>
  </div>
</template>

<script setup>
const props = defineProps({
  categories: { type: Array, required: true }
});

const emit = defineEmits(['update', 'delete']);
</script>
