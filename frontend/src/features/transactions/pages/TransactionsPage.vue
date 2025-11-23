<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Transactions</h1>
        <p class="page-subtitle">Учет движений по счетам</p>
      </div>

      <div class="page-actions">
        <button class="btn btn-primary" @click="openCreate">Add transaction</button>
      </div>
    </header>
    <div v-if="accounts.length">

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
      <p>Add <a href="/accounts">Account</a></p>
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
  await Promise.all([load(page.value, size.value)]);
});

watch(
    () => route.query,
    (q) => {
      page.value = Number(q.page ?? 0);
      size.value = Number(q.size ?? 10);
      load(page.value, size.value);
    }
);

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

function onAdd(payload) {
  addTransaction({
    name: payload.name,
    amount: Number(payload.amount || 0),
    accountId: Number(payload.accountId),
    categoryId: Number(payload.categoryId),
    createdAt: payload.createdAt,
    latitude: payload.latitude,
    longitude: payload.longitude,
  });
  closeCreate();
  setPage(0);
}

function onDelete(payload) {
  deleteTransactionById(payload.id);
  setPage(0);
}

function onUpdate(payload) {
  updateTransactionById(payload.id, {
    name: payload.name,
    amount: Number(payload.amount || 0),
    categoryId: Number(payload.categoryId),
    createdAt: payload.createdAt,
    latitude: payload.latitude,
    longitude: payload.longitude,
  });
  closeEdit();
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
  if (newPage < 0) return;
  if (totalPages.value && newPage >= totalPages.value) return;
  await load(newPage, size.value);
  router.replace({
    query: {
      page: page.value,
      size: size.value,
    }
  });
}

const messageClass = computed(() => ({
  'green-text': messageOk.value === true,
  'red-text': messageOk.value === false,
}));
</script>
