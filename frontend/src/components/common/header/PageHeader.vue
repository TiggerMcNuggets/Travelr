<template>
  <div v-if="isExtraSmall">
    <PageHeaderMobile
      :title="title"
      :options="options"
      :multiOptions="multiOptions"
      :undo="undo"
      :redo="redo"
      :canUndo="canUndo"
      :canRedo="canRedo"
      :disableUndoRedo="disableUndoRedo"
      :enableBackButton="enableBackButton"
      :backButtonOverride="backButtonOverride"
    />
  </div>
  <div v-else>
    <PageHeaderDesktop
      :title="title"
      :options="options"
      :multiOptions="multiOptions"
      :undo="undo"
      :redo="redo"
      :canUndo="canUndo"
      :canRedo="canRedo"
      :disableUndoRedo="disableUndoRedo"
      :enableBackButton="enableBackButton"
      :backButtonOverride="backButtonOverride"
    />
  </div>
</template>

<style >
.back-button-icon {
  font-size: 40px;
}

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
import UndoRedoButtons from "../../common/rollback/UndoRedoButtons.vue";
import DeviceSizeMixin from "../../mixins/DeviceSizeMixin.vue";
import PageHeaderDesktop from "./PageHeaderDesktop.vue";
import PageHeaderMobile from "./PageHeaderMobile.vue";

export default {
  // Generic page header to be reused on pages and make consistent.
  name: "PageHeader",
  mixins: [DeviceSizeMixin],
  components: {
    PageHeaderDesktop,
    PageHeaderMobile,
    UndoRedoButtons
  },

  // Props used by the component
  props: {
    title: String,
    options: Array,
    multiOptions: Array,
    undo: Function,
    redo: Function,
    canUndo: Function,
    canRedo: Function,
    disableUndoRedo: Boolean,
    enableBackButton: Boolean,
    backButtonOverride: Function,
  }
};
</script>
