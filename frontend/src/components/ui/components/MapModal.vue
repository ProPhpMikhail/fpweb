<template>
  <Teleport to="body">
    <div
        class="modal fade show d-block"
        tabindex="-1"
        @click.self="onClose"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5">
              {{ selectable ? 'Выбор точки на карте' : 'Местоположение транзакции' }}
            </h1>
            <button
                type="button"
                class="btn-close"
                aria-label="Close"
                @click="onClose"
            ></button>
          </div>

          <div class="modal-body">
            <p v-if="selectable" class="text-muted mb-2">
              Кликните по карте, чтобы поставить маркер. Затем нажмите «Выбрать эту точку».
            </p>

            <GoogleMap
                v-if="center"
                :center="center"
                :zoom="zoom"
                map-type-id="roadmap"
                :options="mapOptions"
                style="width: 100%; height: 400px;"
                @click="onMapClick"
            >
              <Marker
                  v-if="marker"
                  :options="{ position: marker }"
              />
            </GoogleMap>

            <p v-else class="text-muted">
              Координаты не заданы
            </p>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="onClose">
              Закрыть
            </button>
            <button
                v-if="selectable"
                type="button"
                class="btn btn-primary"
                :disabled="!marker"
                @click="onSelect"
            >
              Выбрать эту точку
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="modal-backdrop fade show"></div>
  </Teleport>
</template>

<script setup>
import {ref, watch} from 'vue';
import { GoogleMap, Marker } from 'vue3-google-map';

const props = defineProps({
  latitude: { type: Number, default: null },
  longitude: { type: Number, default: null },
  selectable: { type: Boolean, default: false },
});

const emit = defineEmits(['close', 'select']);

const zoom = 15;

watch(
    () => props,
    (props) => {
      if (!props.latitude || !props.longitude) return;

    },
    { deep: true }
);

const center = ref(
    props.latitude != null && props.longitude != null
        ? { lat: props.latitude, lng: props.longitude }
        : null
);

const marker = ref(
    props.latitude != null && props.longitude != null
        ? { lat: props.latitude, lng: props.longitude }
        : null
);

const mapOptions = {
  disableDefaultUI: false,
  zoomControl: true,
};

function onClose() {
  emit('close');
}

function onMapClick(ev) {
  if (!props.selectable || !ev.latLng) {
    return;
  }
  marker.value = {
    lat: ev.latLng.lat(),
    lng:  ev.latLng.lng()
  };
}

function onSelect() {
  if (!marker.value) return;
  emit('select', { latitude: marker.value.lat, longitude: marker.value.lng });
}
</script>
