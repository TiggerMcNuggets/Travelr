<template>
  <v-container fluid>
    <v-layout row wrap fill-height>
      <v-flex>
          <!-- Right side of page header with optional back button and page title -->
          <div class="page__title_container">
            <v-btn
              color="error"
              flat
              fab
              @click="$router.go(-1)"
              v-if="enableBackButton && !backButtonOverride"
            >
              <v-icon class="back-button-icon">keyboard_arrow_left</v-icon>
            </v-btn>
            <v-btn
              color="error"
              flat
              fab
              @click="backButtonOverride"
              v-if="enableBackButton && backButtonOverride"
            >
              <v-icon class="back-button-icon">keyboard_arrow_left</v-icon>
            </v-btn>

            <h1 class="page-title" :class="enableBackButton ? 'h1_space' : ''">{{title}}</h1>
          </div>
          <v-layout>
            <UndoRedoButtons
            v-if="!disableUndoRedo"
            :canRedo="canRedo()"
            :canUndo="canUndo()"
            :undo="undo"
            :redo="redo"
            ></UndoRedoButtons>
            <div v-if="options">
              <v-tooltip bottom v-for="(option, i) in options" :key="i">
                <template v-slot:activator="{ on }">
                  <v-btn
                    v-on="on"
                    color="error"
                    flat
                    fab
                    class="header-button flat"
                    @click="option.action"
                  >
                    <v-icon dark>{{option.icon}}</v-icon>
                  </v-btn>
                </template>
                <span>{{option.title}}</span>
              </v-tooltip>
            </div>

            <div v-if="multiOptions">
              <v-tooltip bottom v-for="(option, i) in multiOptions" :key="i">
                <template v-slot:activator="{ onHover }">
                  <v-menu>
                    <template v-slot:activator="{ on }">
                      <v-btn class="header-button" icon v-on="on" color="error" flat fab>
                        <v-icon dark>{{option.icon}}</v-icon>
                      </v-btn>
                    </template>

                    <v-list>
                      <v-list-tile
                        v-for="(action, i) in option.actions"
                        :key="i"
                        @click="action.callback"
                      >
                        <v-list-tile-title>{{ action.text }}</v-list-tile-title>
                      </v-list-tile>
                    </v-list>
                  </v-menu>
                </template>
                <span>{{option.title}}</span>
              </v-tooltip>
            </div>
          </v-layout>
        <v-divider class="photo-header-divider"></v-divider>
      </v-flex>
    </v-layout>
  </v-container>
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

export default {
  // Generic page header to be reused on pages and make consistent.
  name: "PageHeader",
  mixins: [DeviceSizeMixin],
  components: {
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
