<template>
  <div>
    <v-btn
      class="upload-toggle-button"
      fab
      small
      flat
      :disabled="!canUndo"
      @click="undo"
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

    // Detects the ctrl z event and executes the undo operation.
    window.addEventListener(
      "keyup",
      function(event) {
        if (event.keyCode == 90 && event.ctrlKey) {
          this.undo();
        }
      }.bind(this)
    );

    // Detects the ctrl y event and executes the redo operation.
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
