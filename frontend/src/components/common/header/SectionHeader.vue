

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

      <!-- <v-btn
        icon
        @click="() => {searchActive = false; createGroupActive=!createGroupActive}"
        flat
        small
        color="white"
        v-on="on"
      >
        <v-icon>add</v-icon>
      </v-btn>
      <v-btn
        icon
        @click="() => {searchActive = !searchActive; createGroupActive=false}"
        flat
        small
        color="white"
        v-on="on"
      >
        <v-icon>search</v-icon>
      </v-btn>-->

      <!-- Section options such as add or search to be displayed on the right side of the header -->
      <div v-if="options">
        <!-- <v-btn
          v-for="option in options.entries()"
          :key="option[0]"
          class="upload-toggle-button"
          fab
          small
          dark
          color="indigo"
        >
          <v-icon dark>{{option[1].icon}}</v-icon>
        </v-btn>-->
        <v-btn
          v-for="option in options.entries()"
          :key="option[0]"
          icon
          @click="option[1].action"
          flat
          small
          color="black"
          v-on="on"
        >
          <v-icon>{{option[1].icon}}</v-icon>
        </v-btn>
      </div>
    </v-flex>
  </v-layout>
</template>

<script>
import UndoRedoButtons from "../../common/rollback/UndoRedoButtons.vue";

export default {
  // Generic page header to be reused on pages and make consistent.
  name: "PageHeader",

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
