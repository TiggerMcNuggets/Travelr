<template>
  <div>
    <div class="section">
      <h2 class="headline">{{title}}</h2>
      <UndoRedoButtons
        :canRedo="rollbackCanRedo()"
        :canUndo="rollbackCanUndo()"
        :undo="undo"
        :redo="redo"
      ></UndoRedoButtons>

      <div>
        <v-btn
          v-for="option in options.entries()"
          :key="option[0]"
          class="upload-toggle-button"
          fab
          small
          dark
          color="error"
          @click="option[1].action"
        >
          <v-icon dark>{{option[1].icon}}</v-icon>
        </v-btn>
      </div>
    </div>

    <v-divider class="photo-header-divider"></v-divider>
  </div>
</template>

<style>
.section {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>

<script>
import RollbackMixin from "../../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../../common/rollback/UndoRedoButtons.vue";

export default {
  name: "PageHeader",

  mixins: [RollbackMixin],

  components: {
    UndoRedoButtons
  },

  props: {
    title: String,
    options: Array,
    undo: Function,
    redo: Function
  }
};
</script>
