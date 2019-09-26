<template>
  <div>
    <v-tooltip bottom >
      <template v-slot:activator="{ on }">
        <span v-on="on">
          <v-btn class="header-button" fab flat :disabled="!canUndo" @click="undo">
            <v-icon dark>undo</v-icon>
          </v-btn>
        </span>
      </template>
      <span>Undo</span>
    </v-tooltip>
    <v-tooltip bottom >
      <template v-slot:activator="{ on }">
        <span v-on="on">
          <v-btn class="header-button" v-on="on" fab flat :disabled="!canRedo" @click="redo">
            <v-icon dark>redo</v-icon>
          </v-btn>
        </span>
      </template>
      <span>Redo</span>
    </v-tooltip>
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
