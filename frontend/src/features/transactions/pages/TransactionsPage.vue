<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Транзакции</h1>
        <p class="page-subtitle">Учет движений по счетам</p>
      </div>
    </header>
    <div v-if="accounts.length">

      <TransactionFilterForm
        @applyFilter="applyFilter"
        :initial="filter"
        :accounts="accounts"
        :categories="categories"
      />

      <div class="summary" v-if="summaryType">
        <p v-if="summaryType === 'expense'">Суммарный <span style="color: red">расход</span> по счету: <span>{{ summaryAmount }}</span></p>
        <p v-if="summaryType === 'income'">Суммарный <span style="color: green">приход</span> по счету: <span>{{ summaryAmount }}</span></p>
      </div>

      <div class="page-actions" v-if="accounts.length">
        <button class="btn btn-primary" @click="openCreate">Добавить</button>
      </div>

      <p v-if="loading">Loading...</p>

      <TransactionsTable
          :transactions="transactions"
          @update="openEdit"
          @delete="onDelete"
          @openMap="openMap"
      />

      <p :class="messageClass">{{ message }}</p>

      <BaseModal
          v-if="isCreateOpen"
          title="Add transaction"
          @close="closeCreate"
      >
        <TransactionForm
            :accounts="accounts"
            :initial="initial"
            :categories="categories"
            @submit="onAdd"
            @cancel="closeCreate"
            @openMap="openMap"
        />
      </BaseModal>

      <BaseModal
          v-if="isEditOpen && initial"
          title="Edit transaction"
          @close="closeEdit"
      >
        <TransactionForm
            :categories="categories"
            :initial="initial"
            @submit="onUpdate"
            @cancel="closeEdit"
            @openMap="openMap"
        />
      </BaseModal>

      <MapModal
          v-if="isMapOpen && initial.latitude && initial.longitude"
          :latitude="Number(initial.latitude)"
          :longitude="Number(initial.longitude)"
          :selectable="selectable"
          @close="closeMap"
          @select="selectLocation"
      />

      <Pagination
          :page="page"
          :totalPages="totalPages"
          :totalElements="totalElements"
          @onChange="setPage"
      />

    </div>
    <div v-else>
      <p>Добавить <a href="/accounts">счет</a></p>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref, watch} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTransactions } from '@/features/transactions/composables/useTransactions';
import TransactionsTable from '@/features/transactions/components/TransactionsTable.vue';
import TransactionForm from '@/features/transactions/components/TransactionForm.vue';
import BaseModal from '@/components/ui/components/BaseModal.vue';
import MapModal from '@/components/ui/components/MapModal.vue';
import Pagination from "@/components/ui/components/Pagination.vue";
import TransactionFilterForm from "@/features/transactions/components/TransactionFilterForm.vue";

const {
  transactions,
  accounts,
  categories,
  loading,
  message,
  messageOk,
  page,
  size,
  totalPages,
  totalElements,
  summaryAmount,
  summaryType,
  load,
  addTransaction,
  updateTransactionById,
  deleteTransactionById,
} = useTransactions();

const route = useRoute();
const router = useRouter();
const isCreateOpen = ref(false);
const isEditOpen = ref(false);
const initial = reactive({});
const isMapOpen = ref(false);
const selectable = ref(true);
const defaultLatitude = 55.751244;
const defaultLongitude = 37.618423;

onMounted(async () => {
  await Promise.all([load(page.value, size.value, filter)]);
});

const filter = reactive({
  accountId: route.query.accountId ?? '',
  categoryId: route.query.categoryId ?? '',
  name: route.query.name ?? '',
  type: route.query.type ?? '',
  amountFrom: route.query.amountFrom ?? '',
  amountTo: route.query.amountTo ?? '',
  createdAtFrom: route.query.createdAtFrom ?? '',
  createdAtTo: route.query.createdAtTo ?? '',
});

watch(
    () => route.query,
    (q) => {
      page.value = Number(q.page ?? 1);
      size.value = Number(q.size ?? 10);

      filter.accountId  = q.accountId  ?? '';
      filter.categoryId = q.categoryId ?? '';
      filter.name = q.name ?? '';
      filter.type = q.type ?? '';
      filter.amountFrom = q.amountFrom ?? '';
      filter.amountTo = q.amountTo ?? '';
      filter.createdAtFrom = q.createdAtFrom ?? '';
      filter.createdAtTo = q.createdAtTo ?? '';

      load(page.value, size.value, filter);
    }
);

function getQuery() {
  const query = {
    page: page.value,
    size: size.value,
  };

  Object.entries(filter).forEach(([key, value]) => {
    if (value !== '' && value != null) {
      query[key] = value;
    }
  });

  return query;
}

async function applyFilter(payload) {
  page.value = 1;
  Object.entries(payload).forEach(([key, value]) => {
    filter[key] = value
  });
  router.replace({
    query: getQuery(),
  });
}

function selectLocation({ latitude, longitude }) {
  initial.latitude = latitude.toFixed(6);
  initial.longitude = longitude.toFixed(6);
  isMapOpen.value = false;
}

function closeMap() {
  isMapOpen.value = false;
  selectable.value = false;
}

async function openMap(coords, editable) {
  isMapOpen.value = true;
  selectable.value = editable;


  const {currentLat, currentLng} = await getUserLocation();
  initial.latitude = coords.latitude ?? currentLat ?? defaultLatitude;
  initial.longitude = coords.longitude ?? currentLng ?? defaultLongitude;
}

function openCreate() {
  isCreateOpen.value = true;
}
function closeCreate() {
  isCreateOpen.value = false;
  for (var field in initial) {
    initial[field] = '';
  }
}
function openEdit(t) {
  Object.assign(initial, t);
  isEditOpen.value = true;
}
function closeEdit() {
  isEditOpen.value = false;
  for (var field in initial) {
    initial[field] = '';
  }
}

async function onAdd(payload) {
  await addTransaction({
    name: payload.name,
    amount: Number(payload.amount || 0),
    accountId: Number(payload.accountId),
    categoryId: Number(payload.categoryId),
    createdAt: payload.createdAt,
    latitude: payload.latitude,
    longitude: payload.longitude,
  });
  closeCreate();
  setPage(1);
}

async function onDelete(payload) {
  await deleteTransactionById(payload.id);
  setPage(1);
}

async function onUpdate(payload) {
  await updateTransactionById(payload.id, {
    name: payload.name,
    amount: Number(payload.amount || 0),
    categoryId: Number(payload.categoryId),
    createdAt: payload.createdAt,
    latitude: payload.latitude,
    longitude: payload.longitude,
  });
  closeEdit();
  setPage(page.value);
}

async function getUserLocation() {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      alert('Геолокация не поддерживается браузером');
      return reject('no_support');
    }

    navigator.geolocation.getCurrentPosition(
        (pos) => {
          const { latitude, longitude } = pos.coords;
          resolve({
            currentLat: latitude.toFixed(6),
            currentLng: longitude.toFixed(6),
          });
        },
        (err) => {
          resolve({
            currentLat: null,
            currentLng: null,
          });
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0,
        }
    );
  });
}

async function setPage(newPage) {
  if (newPage < 1) return;
  if (totalPages.value && newPage > totalPages.value) return;
  await load(newPage, size.value, filter);
  router.replace({
    query: getQuery()
  });
}

const messageClass = computed(() => ({
  'green-text': messageOk.value === true,
  'red-text': messageOk.value === false,
}));
</script>
