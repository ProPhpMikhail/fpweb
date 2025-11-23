<template>
  <nav v-if="totalPages > 1" class="mt-3">
    <ul class="pagination">
      <li class="page-item" :class="{ disabled: page === 0 }">
        <button
            class="page-link"
            @click="page > 0 && emit('onChange', page - 1)"
            :disabled="page === 0"
        >
          «
        </button>
      </li>
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
          {{ item.value + 1 }}
        </button>
        <span v-else class="page-link disabled">…</span>
      </li>
      <li class="page-item" :class="{ disabled: page >= totalPages - 1 }">
        <button
            class="page-link"
            @click="page < totalPages - 1 && emit('onChange', page + 1)"
            :disabled="page >= totalPages - 1"
        >
          »
        </button>
      </li>
    </ul>

    <div class="text-muted">
      Страница {{ page + 1 }} из {{ totalPages }},
      всего {{ totalElements }} записей
    </div>
  </nav>
</template>

<script setup>
import {computed, ref, watch} from "vue";

const props = defineProps({
  page: Number,
  totalPages: Number,
  totalElements: Number,
});

const emit = defineEmits(['onChange']);

function getPagionation() {
  const items = [];
  const total = props.totalPages || 0;
  const current = props.page || 0;

  if (total <= 1) return items;

  if (total <= 7) {
    for (let i = 0; i < total; i++) {
      items.push({ type: 'page', value: i });
    }
    return items;
  }

  const delta = 1;
  const left = current - delta;
  const right = current + delta;

  const range = [];
  for (let i = 0; i < total; i++) {
    if (
        i === 0 ||
        i === total - 1 ||
        (i >= left && i <= right)
    ) {
      range.push(i);
    }
  }

  let prev = null;
  for (const p of range) {
    if (prev !== null && p - prev > 1) {
      items.push({ type: 'ellipsis' });
    }
    items.push({ type: 'page', value: p });
    prev = p;
  }

  return items;
}

const paginationItems = computed(() => {
  return getPagionation();
});

</script>