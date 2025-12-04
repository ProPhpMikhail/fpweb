<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Счета</h1>
        <p class="page-subtitle">Список счетов</p>
      </div>

      <div class="page-actions">
        <button class="btn btn-primary" @click="openCreate">Добавить</button>
      </div>
    </header>

    <p v-if="loading">Loading...</p>

    <AccountTable
        :initial="initial"
        :accounts="accounts"
        @update="openEdit"
        @delete="onDelete"
    />

    <br>

    <p :class="messageClass">{{ message }}</p>

    <BaseModal
        v-if="isEditOpen"
        title="Edit account"
        @close="closeEdit"
    >
      <AccountForm
          :initial="initial"
          :currencies="currencies"
          @submit="onUpdate"
          @cancel="closeEdit"
      />
    </BaseModal>

    <BaseModal
        v-if="isCreateOpen"
        title="Add account"
        @close="closeCreate"
    >
      <AccountForm
          :initial="initial"
          :currencies="currencies"
          @submit="onAdd"
          @cancel="closeCreate"
      />
    </BaseModal>

    <Pagination
        :page="page"
        :totalPages="totalPages"
        :totalElements="totalElements"
        @onChange="setPage"
    />
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue';
import AccountForm from "@/features/accounts/components/AccountForm.vue";
import {useAccounts} from "@/features/accounts/composables/useAccounts";
import AccountTable from "@/features/accounts/components/AccountTable.vue";
import BaseModal from "@/components/ui/components/BaseModal.vue";
import Pagination from "@/components/ui/components/Pagination.vue";
import router from "@/router";

const {
  accounts,
  currencies,
  loading,
  message,
  messageOk,
  page,
  size,
  totalPages,
  totalElements,
  load,
  addAccount,
  updateAccountById,
  deleteAccountById,
  clearAll,
} = useAccounts();

const isCreateOpen = ref(false);
const isEditOpen = ref(false);
const initial = reactive({});

onMounted(async () => {
  await Promise.all([load(page.value, size.value)]);
});

function openCreate() {
  isCreateOpen.value = true;
}
function closeCreate() {
  isCreateOpen.value = false;
  for (var field in initial) {
    initial[field] = '';
  }
}
function openEdit(payload) {
  Object.assign(initial, payload);
  isEditOpen.value = true;
}
function closeEdit() {
  isEditOpen.value = false;
  for (var field in initial) {
    initial[field] = '';
  }
}

function onAdd(payload) {
  addAccount({
    name: payload.name,
    sort: payload.sort,
    balance: Number(payload.balance || 0),
    currency: payload.currency,
  });
  closeCreate();
  setPage(1);
}

function onUpdate(payload) {
  updateAccountById(payload.id, {
    name: payload.name,
    sort: Number(payload.sort || 1),
    balance: Number(payload.balance),
  });
  closeEdit();
}

function onDelete(payload) {
  deleteAccountById(payload.id);
  setPage(1);
}

async function setPage(newPage) {
  if (newPage < 1) return;
  if (totalPages.value && newPage > totalPages.value) return;
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

<style scoped>
.red-text {
  color: red;
}
.green-text {
  color: green;
}
</style>
