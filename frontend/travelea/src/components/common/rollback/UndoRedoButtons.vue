<template>
  <div>
    <v-btn
      class="upload-toggle-button"
      fab
      small
      flat
      :disabled="!canUndo"
      @click="undo"
      @keyup.enter="undo"
    >
      <v-icon dark>undo</v-icon>
    </v-btn>
    <v-btn class="upload-toggle-button" fab small flat :disabled="!canRedo" @click="redo">
      <v-icon dark>redo</v-icon>
    </v-btn>
  </div>
</template>

<script>
export default {
  props: {
    canUndo: Boolean,
    canRedo: Boolean,
    undo: Function,
    redo: Function
  },

  mounted() {
    window.addEventListener(
      "keyup",
      function(event) {
        if (event.keyCode == 90 && event.ctrlKey) {
          this.undo();
        }
      }.bind(this)
    );

    window.addEventListener(
      "keyup",
      function(event) {
        if (event.keyCode == 89 && event.ctrlKey) {
          this.redo();
        }
      }.bind(this)
    );
  }
};
</script>
