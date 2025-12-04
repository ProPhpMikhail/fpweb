<template>
  <div class="page">
    <header class="page-header">
      <div>
        <h1 class="page-title">Категории</h1>
        <p class="page-subtitle">Категории трат</p>
      </div>

      <div class="page-actions">
        <button class="btn btn-primary" @click="openCreate">Добавить</button>
      </div>
    </header>

    <p v-if="loading">Loading...</p>

    <CategoriesTable
        :categories="categories"
        @update="openEdit"
        @delete="onDelete"
    />

    <p :class="messageClass">{{ message }}</p>

    <BaseModal
        v-if="isEditOpen"
        title="Edit category"
        @close="closeEdit"
    >
      <CategoryForm
          :categories="categories"
          :initial="initial"
          @submit="onUpdate"
          @cancel="closeEdit"
      />
    </BaseModal>

    <BaseModal
        v-if="isCreateOpen"
        title="Add category"
        @close="closeCreate"
    >
      <CategoryForm
          :initial="initial"
          :categories="categories"
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
import { useCategories } from '@/features/categories/composables/useCategories';
import CategoriesTable from "@/features/categories/components/CategoriesTable.vue";
import CategoryForm from "@/features/categories/components/CategoryForm.vue";
import BaseModal from "@/components/ui/components/BaseModal.vue";
import Pagination from "@/components/ui/components/Pagination.vue";
import router from "@/router";

const {
  categories,
  loading,
  message,
  messageOk,
  page,
  size,
  totalPages,
  totalElements,
  load,
  addCategory,
  updateCategoryById,
  deleteCategoryById,
} = useCategories();

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
  addCategory({
    name: payload.name,
    sort: Number(payload.sort || 1),
  });
  closeCreate();
  setPage(1);
}

function onUpdate(payload) {
  updateCategoryById(payload.id, {
    name: payload.name,
    sort: Number(payload.sort || 1),
  });
  closeEdit();
}

function onDelete(payload) {
  deleteCategoryById(payload.id);
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
