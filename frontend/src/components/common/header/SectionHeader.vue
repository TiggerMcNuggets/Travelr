

<template>
  <v-layout class="section-heading">
    <h3>{{title}}</h3>

    <v-spacer/>

    <v-flex class="section-options-container">
      <UndoRedoButtons
        v-if="!disableUndoRedo"
        :canRedo="canRedo()"
        :canUndo="canUndo()"
        :undo="undo"
        :redo="redo"
      ></UndoRedoButtons>
      <div v-if="options">
        <v-btn
          v-for="option in options.entries()"
          :key="option[0]"
          icon
          @click="option[1].action"
          flat
          small
          color="black"
        >
          <v-icon>{{option[1].icon}}</v-icon>
        </v-btn>
      </div>
    </v-flex>
  </v-layout>
</template>

<script>
import UndoRedoButtons from "../rollback/UndoRedoButtons.vue";

export default {
  // Generic page header to be reused on pages and make consistent.
  name: "SectionHeader",

  components: {
    UndoRedoButtons
  },

  // Props used by the component
  props: {
    title: String,
    options: Array,
    undo: Function,
    redo: Function,
    canUndo: Function,
    canRedo: Function,
    disableUndoRedo: Boolean,
    enableBackButton: Boolean,
    backButtonOverride: Function
  }
};
</script>
