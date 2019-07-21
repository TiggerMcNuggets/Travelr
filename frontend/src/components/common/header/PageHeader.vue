<template>
  <div>
    <div class="section">
      <div class="page__title_container">
        <v-btn fab small dark color="indigo" @click="$router.go(-1)" v-if="enableBackButton">
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>

        <h1 class="page-title" :class="enableBackButton ? 'h1_space' : ''">{{title}}</h1>
      </div>

      <v-spacer/>

      <UndoRedoButtons
        v-if="!disableUndoRedo"
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
          color="indigo"
          @click="option[1].action"
        >
          <v-icon dark>{{option[1].icon}}</v-icon>
        </v-btn>
      </div>
    </div>

    <v-divider class="photo-header-divider"></v-divider>
  </div>
</template>

<style >
.page__title_container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.page__title_container .h1_space {
  margin-left: 15px;
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
    redo: Function,
    disableUndoRedo: Boolean,
    enableBackButton: Boolean
  }
};
</script>
