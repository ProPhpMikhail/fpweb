<template>
  <form @submit.prevent="onSubmit">
    <div class="form-row align-items-center">
      <div class="col-auto">
        <label class="sr-only" for="name">Name</label>
        <input
            class="form-control mb-2"
            id="name"
            v-model="local.name"
            type="text"
            placeholder="Name"
        >
      </div>
      <div class="col-auto">
        <label class="sr-only" for="name">Sort</label>
        <input
            class="form-control mb-2"
            id="sort"
            v-model="local.sort"
            type="text"
            placeholder="Sort"
        >
      </div>
      <div class="col-auto">
        <button class="btn btn-primary mb-2" type="submit">
          Save
        </button>
      </div>
    </div>
  </form>
</template>

<script setup>

import {reactive} from "vue";
const props = defineProps({
  initial: {type: Object, required: false },
});

const local = reactive({
  id: props.initial.id ?? '',
  name: props.initial.name ?? '',
  sort: props.initial.sort ?? '',
});

const emit = defineEmits(['submit']);

function onSubmit() {
  emit('submit', {...local});
  local.name = '';
  local.sort = '';
}
</script>
