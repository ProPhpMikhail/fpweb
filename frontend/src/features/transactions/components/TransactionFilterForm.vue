<template>
  <div class="form-row align-items-center">
    <div class="col-auto my-1">
      <select class="form-control mb-2" v-model="filter.accountId">
        <option value="">Счета</option>
        <option
            v-for="acc in accounts"
            :key="acc.id"
            :value="acc.id"
        >
          {{ acc.name }}
        </option>
      </select>
    </div>

    <div class="col-auto my-1">
      <select class="form-control mb-2" v-model="filter.categoryId">
        <option value="">Категории</option>
        <option
            v-for="cat in categories"
            :key="cat.id"
            :value="cat.id"
        >
          {{ cat.name }}
        </option>
      </select>
    </div>

    <select class="form-control mb-2" v-model="filter.type">
      <option value="">Тип</option>
      <option value="expense">Расход</option>
      <option value="income">Приход</option>
    </select>

    <div class="col-auto my-1">
      <input class="form-control" v-model="filter.name" placeholder="Имя" />
    </div>

    <div class="col-auto my-1">
      <input class="form-control" v-model="filter.amountFrom" type="number" placeholder="Сумма от" />
    </div>

    <div class="col-auto my-1">
      <input class="form-control" v-model="filter.amountTo" type="number" placeholder="Сумма до" />
    </div>

    <div class="col-auto my-1">
      <input class="form-control" v-model="filter.createdAtFrom" type="datetime-local" />
    </div>
    <div class="col-auto my-1">
      <input class="form-control" v-model="filter.createdAtTo" type="datetime-local" />
    </div>
    <div class="col-auto my-1">
      <button class="btn btn-primary mb-2" @click="emit('applyFilter', filter)">Фильтр</button>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";

const props = defineProps({
  accounts: {type: Object, required: true},
  categories: {type: Object, required: true},
  initial: {type: Object, required: true},
});


const emit = defineEmits(['applyFilter']);

const filter = reactive({
  name: props.initial.name ?? '',
  type: props.initial.type ?? '',
  accountId: props.initial.accountId ?? '',
  categoryId: props.initial.categoryId ?? '',
  amountFrom: props.initial.amountFrom ?? '',
  amountTo: props.initial.amountTo ?? '',
  createdAtFrom: props.initial.createdAtFrom ?? '',
  createdAtTo: props.initial.createdAtTo ?? '',
});
</script>
