<template>
  <Teleport to="body">
    <!-- модалка -->
    <div
        class="modal fade show d-block"
        tabindex="-1"
        role="dialog"
        aria-modal="true"
        @click.self="onClose"
    >
      <div class="modal-dialog">
        <div class="modal-content">

          <div class="modal-header">
            <h1 class="modal-title fs-5">
              {{ title }}
            </h1>
            <button
                type="button"
                class="btn-close"
                aria-label="Close"
                @click="onClose"
            ></button>
          </div>

          <div class="modal-body">
            <slot />
          </div>

          <div class="modal-footer" v-if="$slots.footer">
            <slot name="footer" />
          </div>
        </div>
      </div>
    </div>

    <!-- фон -->
    <div class="modal-backdrop fade show"></div>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  title: { type: String, default: '' },
});
const emit = defineEmits(['close']);

function onClose() {
  emit('close');
}
</script>
