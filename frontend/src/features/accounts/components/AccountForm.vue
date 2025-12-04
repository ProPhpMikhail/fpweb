<template>
  <form @submit.prevent="onSubmit">
    <div class="form-row align-items-center">
      <div class="col-auto">
        <label class="sr-only" for="name">Имя</label>
        <input
            class="form-control mb-2"
            id="name"
            v-model="local.name"
            type="text"
            placeholder="Name"
        >
      </div>
      <div class="col-auto">
        <label class="sr-only" for="name">Сортировка</label>
        <input
            class="form-control mb-2"
            id="sort"
            v-model="local.sort"
            type="text"
            placeholder="Sort"
        >
      </div>
      <div class="col-auto">
        <label class="sr-only" for="balance">Баланс</label>
        <input
            class="form-control mb-2"
            id="balance"
            v-model="local.balance"
            type="text"
            placeholder="Balance"
        >
      </div>
      <div class="col-auto" v-if="props.initial?.currency == null">
        <label class="sr-only" for="account">Валюта</label>
        <select
            id="currency"
            class="form-control mb-2"
            v-model="local.currency"
        >
          <option
              v-for="currency in currencies"
              :key="currency.id"
              :value="currency.id"
              :selected="currency.id === local.currency"
          >
            {{ currency.name }}
          </option>
        </select>
      </div>
      <div class="col-auto">
        <button class="btn btn-primary mb-2" type="submit">
          Сохранить
        </button>
      </div>
    </div>
  </form>
</template>

<script setup>
import {reactive} from "vue";

const emit = defineEmits(['submit']);

const props = defineProps({
  initial: { type: Object, required: false },
  currencies: { type: Object, required: true },
});

const local = reactive({
  id: props.initial?.id ?? '',
  name: props.initial?.name ?? '',
  sort: props.initial?.sort ?? '',
  balance: props.initial?.balance ?? '',
  currency: props.initial?.currency ?? '',
});

function onSubmit() {
  emit('submit', {...local});
}
</script>
