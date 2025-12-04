<template>
  <nav v-if="totalPages > 1" class="mt-3">
    <ul class="pagination">
      <!-- Предыдущая страница -->
      <li class="page-item" :class="{ disabled: page <= 1 }">
        <button
            class="page-link"
            @click="page > 1 && emit('onChange', page - 1)"
            :disabled="page <= 1"
        >
          «
        </button>
      </li>

      <!-- Номера страниц + троеточия -->
      <li
          v-for="(item, idx) in paginationItems"
          :key="item.type === 'ellipsis' ? 'e' + idx : 'p' + item.value"
          class="page-item"
          :class="{ active: item.type === 'page' && item.value === page }"
      >
        <button
            v-if="item.type === 'page'"
            class="page-link"
            @click="emit('onChange', item.value)"
        >
          {{ item.value }}
        </button>
        <span v-else class="page-link disabled">…</span>
      </li>

      <!-- Следующая страница -->
      <li class="page-item" :class="{ disabled: page >= totalPages }">
        <button
            class="page-link"
            @click="page < totalPages && emit('onChange', page + 1)"
            :disabled="page >= totalPages"
        >
          »
        </button>
      </li>
    </ul>

    <div class="text-muted">
      Страница {{ page }} из {{ totalPages }},
      всего {{ totalElements }} записей
    </div>
  </nav>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  page: Number,          // 1..totalPages
  totalPages: Number,
  totalElements: Number,
});

const emit = defineEmits(["onChange"]);

function getPagination() {
  const items = [];
  const total = props.totalPages ?? 0;
  const current = props.page ?? 1; // 1-based

  if (total <= 1) return items;

  // Если страниц мало — показываем все
  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      items.push({ type: "page", value: i });
    }
    return items;
  }

  const delta = 1;
  const left = current - delta;
  const right = current + delta;

  const range = [];
  for (let i = 1; i <= total; i++) {
    if (
        i === 1 ||
        i === total ||
        (i >= left && i <= right)
    ) {
      range.push(i);
    }
  }

  let prev = null;
  for (const p of range) {
    if (prev !== null && p - prev > 1) {
      items.push({ type: "ellipsis" });
    }
    items.push({ type: "page", value: p });
    prev = p;
  }

  return items;
}

const paginationItems = computed(() => getPagination());
</script>
