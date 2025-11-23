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
            required
        >
      </div>
      <div class="col-auto" v-if="accounts">
        <label class="sr-only" for="account">Account</label>
        <select
            id="account"
            class="form-control mb-2 form-select"
            v-model="local.accountId"
            required
        >
          <option
              v-for="acc in accounts"
              :key="acc.id"
              :value="acc.id"
          >
            {{ acc.name }}
          </option>
        </select>
      </div>
      <div class="col-auto" v-if="categories">
        <label class="sr-only" for="account">Category</label>
        <select
            id="category"
            class="form-control mb-2 form-select"
            v-model="local.categoryId"
        >
          <option
              v-for="cat in categories"
              :key="cat.id"
              :value="cat.id"
              :selected="local.categoryId === cat.id"
          >
            {{ cat.name }}
          </option>
        </select>
      </div>
      <div class="col-auto">
        <label class="sr-only">
          <span>Тип</span>
          <select class="form-control mb-2" v-model="local.type">
            <option value="expense">Расход</option>
            <option value="income">Приход</option>
          </select>
        </label>
      </div>
      <div class="col-auto">
        <label class="sr-only" for="amount">Amount</label>
        <input
            class="form-control"
            v-model.number="local.amount"
            type="number"
            step="0.01"
            required
        >
      </div>
      <div class="col-auto">
        <label class="sr-only" for="createdAt">Created at</label>
        <input
            id="createdAt"
            class="form-control mb-2"
            type="datetime-local"
            v-model="local.createdAt"
            required
        />
      </div>
      <div class="col-auto">
        <label class="sr-only">
          <span class="">Геопозиция (опционально)</span>
          <div class="mb-4">
            <input
                class="form-control"
                type="hidden"
                placeholder="lat"
                v-model="local.latitude"
            />
            <input
                class="form-control"
                type="hidden"
                placeholder="lng"
                v-model="local.longitude"
            />
            <button type="button" class="btn btn-outline-primary btn-sm" @click="onSelectLocation">
              🗺️ Выбрать на карте
            </button>
          </div>
        </label>
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
import { reactive, watch } from 'vue';
import { toLocalInputValue, nowLocal } from '@/api/helpers';

const props = defineProps({
  accounts: { type: Array, required: false },
  categories: { type: Array, required: false },
  initial: {type: Object, required: false },
});
const local = reactive({
  id: props.initial?.id ?? '',
  name: props.initial?.name ?? '',
  amount: props.initial.amount != null ? Math.abs(props.initial.amount) : 0,
  type:
      props.initial?.amount != null && props.initial?.amount > 0
          ? 'income'
          : 'expense',
  accountId: props.initial?.accountId ?? props?.accounts[0]?.id ?? '',
  categoryId: props.initial?.categoryId ?? props?.categories[0]?.id ?? '',
  createdAt: toLocalInputValue(props.initial?.createdAt) ?? nowLocal(),
  latitude: props.initial?.latitude ?? null,
  longitude: props.initial?.longitude ?? null,
});
const emit = defineEmits(['submit', 'openMap']);
function onSubmit() {
  let amount = Number(local.amount) || 0;

  if (local.type === 'expense') {
    amount = -Math.abs(amount);
  } else {
    amount = Math.abs(amount);
  }
  local.amount = amount;
  emit('submit', {...local});
  local.name = '';
  local.amount = '';
  local.accountId = '';
  local.categoryId = '';
}

watch(
    () => props.initial,
    (v) => {
      if (!v) return;
      if (v.latitude && v.latitude && !v.accountId) {
        local.latitude = v.latitude;
        local.longitude = v.longitude;
        return;
      }
      local.accountId = v.accountId ?? null;
      local.name = v.name ?? '';
      local.amount = v.amount != null ? Math.abs(v.amount) : 0;
      local.createdAt = v.createdAt ?? toLocalInputValue(v.createdAt);
      local.latitude = v.latitude ?? '';
      local.longitude = v.longitude ?? '';
      local.type =
          v.amount != null && v.amount < 0
              ? 'expense'
              : 'income';
    },
    { deep: true }
);

function onSelectLocation() {
  const latitude = local.latitude ? Number(local.latitude) : null;
  const longitude = local.longitude ? Number(local.longitude) : null;
  emit('openMap', { latitude, longitude }, true);
}
</script>
