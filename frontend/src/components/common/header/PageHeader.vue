<template>
  <div>
    <v-breadcrumbs v-if="breadcrumbs" :items="breadcrumbs.items" class="header-breadcrumbs">
      <template v-slot:item="props">
        <v-breadcrumbs-item v-on:click="breadcrumbs.action(props.item.id)">{{ props.item.name }}</v-breadcrumbs-item>
      </template>
    </v-breadcrumbs>
    <div class="section">
      <!-- Right side of page header with optional back button and page title -->
      <div class="page__title_container">
        <v-btn
          fab
          small
          dark
          color="indigo"
          @click="$router.go(-1)"
          v-if="enableBackButton && !backButtonOverride"
        >
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>
        <v-btn
          fab
          small
          dark
          color="indigo"
          @click="backButtonOverride"
          v-if="enableBackButton && backButtonOverride"
        >
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>

        <h1 class="page-title" :class="enableBackButton ? 'h1_space' : ''">{{title}}</h1>
      </div>

      <v-spacer />

      <!-- Optional undo redo buttons -->
      <UndoRedoButtons
        v-if="!disableUndoRedo"
        :canRedo="canRedo()"
        :canUndo="canUndo()"
        :undo="undo"
        :redo="redo"
      ></UndoRedoButtons>

      <!-- Page options such as add or search to be displayed on the right side of the header -->
      <div v-if="options">
        <v-tooltip bottom v-for="(option, i) in options" :key="i">
          <template v-slot:activator="{ on }">
            <!-- --------- BUTTON VARIATION WITH TEXT --------- -->
            <!-- --------- TODO: CHECK WITH TEAM WHICH VARIANT IS PERFEFFED ---------- -->
            <!-- <v-btn
              v-on="on"
              color='error'
              :flat="!option.title"
              :small="!option.title"
              :fab="!option.title"
              :class="option.title !== '' ? 'header-button flat' : 'flat fab small header-button'"
              @click="option.action"
            >
            <v-icon :class="option.title ? 'header-button-icon' : ''" dark>{{option.icon}}</v-icon>
            {{option.title}}
            </v-btn> -->
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
        <v-menu v-for="(option, i) in multiOptions" :key="i">
          <template v-slot:activator="{ on }">
            <v-btn class="header-button" icon v-on="on" fab small dark color="indigo">
              <v-icon dark>{{option.icon}}</v-icon>
              {{option.title}}
            </v-btn>
          </template>

          <v-list>
            <v-list-tile v-for="(action, i) in option.actions" :key="i" @click="action.callback">
              <v-list-tile-title>{{ action.text }}</v-list-tile-title>
            </v-list-tile>
          </v-list>
        </v-menu>
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
    multiOptions: Array,
    undo: Function,
    redo: Function,
    canUndo: Function,
    canRedo: Function,
    disableUndoRedo: Boolean,
    enableBackButton: Boolean,
    backButtonOverride: Function,
    breadcrumbs: Object
  }
};
</script>
