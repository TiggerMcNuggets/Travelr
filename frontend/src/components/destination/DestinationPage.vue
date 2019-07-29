<template>
  <v-layout row wrap>
    <!-- Main Nav -->
    <v-flex xs12>
      <v-layout row>
        <v-flex v-if="browseActive" xs4 sm3 md2>
          <DestinationNav
            :destinations="destinations"
            :isDestinationShowing="isDestinationShowing"
            :focusDestination="focusDestination"
            :toggleDestination="toggleDestination"
            :closeDestinationNav="() => { browseActive = false }"
          ></DestinationNav>
        </v-flex>
        <v-flex>
          <DestinationMap
            :destinations="visibleDestinations"
            :focussedDestination="deepCopy(focussedDestination)"
            :focusDestination="focusDestination"
            :openDestinationNav="() => { browseActive = !browseActive }"
          ></DestinationMap>
        </v-flex>

        <v-flex xs4 sm3 md2 v-if="focussedDestination">
          <v-flex
            v-if="focussedDestination.id && isAllowedToEdit"
            d-flex
            align-start
            pb-1
            pl-2
            class="undo-redo-buttons"
          >
            <UndoRedoButtons
              :canRedo="rollbackCanRedo()"
              :canUndo="rollbackCanUndo()"
              :undo="undo"
              :redo="redo"
            ></UndoRedoButtons>
          </v-flex>
          <DestinationDetails
            v-if="focussedDestination"
            :focussedDestination="deepCopy(focussedDestination)"
            :passBackDestination="submitDestination"
            :focusDestination="focusDestination"
            :cancelEdit="cancelEdit"
            :allowedToEdit="isAllowedToEdit"
          ></DestinationDetails>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>


<style>
.browse-button {
  position: absolute;
  z-index: 10;
}

.undo-redo-buttons {
  background-color: white;
}

.menu-item:hover {
  background-color: rgba(0, 0, 0, 0.87);
}
</style>


<script>
import { store } from "../../store/index";
import RollbackMixin from "../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons.vue";
import DestinationNav from "./DestinationNav";
import DestinationMap from "./DestinationMap";
import DestinationDetails from "./DestinationDetails";
import { deepCopy } from "../../tools/deepCopy";
import StoreDestinationsMixinVue from '../mixins/StoreDestinationsMixin.vue';

export default {
  store,
  mixins: [RollbackMixin, StoreDestinationsMixinVue],
  components: {
    UndoRedoButtons,
    DestinationNav,
    DestinationMap,
    DestinationDetails
  },

  data() {
    return {
      focussedDestination: {},
      browseActive: false,
      isShowing: [],

      // privileges data
      isAdminUser: false,
      userId: store.getters.getUser.id
    };
  },

  watch: {
    /**
     * Reset back to viewing mode on destination change
     */
    focussedDestination: {
      handler: function(newValue, oldValue) {
        if (oldValue.id !== newValue.id) {
          this.rollbackFlush();
        }
      },
      deep: true
    },

    destinations: {
      handler: function() {
        this.isShowing = [...this.populateIsShowing()];
      }
    }

  },

  methods: {
    /**
     * Creates a deep copy
     */
    deepCopy(value) {
      return deepCopy(value);
    },

    /**
     * Creates an empty destination?? Is this used??
     */
    createDestination() {
      this.focussedDestination = {
        data: {}
      };
    },

    isDestinationShowing(destinationId) {
      return this.isShowing.find(x => x.id === destinationId).isShowing;
    },

    toggleDestination(destination) {
      const index = this.isShowing.findIndex(x => x.id == destination.id);
      this.isShowing[index].isShowing = !this.isShowing[index].isShowing;
    },

    /**
     * Changed the focused destination to a given destination
     */
    focusDestination(destination) {
      this.focussedDestination = this.deepCopy(destination);
      if (this.focussedDestination && this.focussedDestination.id) {
        const newDest = this.destinations.filter(
          dest => dest.id === this.focussedDestination.id
        )[0];
        this.rollbackSetPreviousBody(newDest);
      }
    },


    /**
     * Checks if a destination exists if it does, updates it. If it doesn't, a new destination is created.
     */
    submitDestination(destination) {
      if (destination.id) {
        this._putDestination(
          this.userId,
          destination.id,
          destination
        )
          .then(() => {
            const url = `/users/${this.userId}/destinations/${
              destination.id
            }`;
            this.rollbackCheckpoint(
              "PUT",
              {
                url: url,
                body: destination
              },
              {
                url: url,
                body: this.rollbackPreviousBody
              }
            );
            this.rollbackPreviousBody = destination;

          })
          .catch(err => {
            console.error(err);
          });
      } else {
        this._postDestination(this.userId, destination)
          .then(() => {
            this.focussedDestination = {};
          })
          .catch(err => {
            console.error(err);
          });
      }
    },

    /**
     * Cancels an edit on a clicked destination.
     */
    cancelEdit() {
      this.focussedDestination = {};
    },

    async fetchAfterRollback() {
      let id = this.focussedDestination.id;
      await this._getDestinations(this.userId);
      this.focussedDestination = this.destinations.filter(
              dest => dest.id === id
      )[0];
    },

    /**
     * Returns the is showing array generated from the list of destinations
     */
    populateIsShowing() {
      return [...this.isShowing, ...this.destinations.filter(dest => !this.isShowing.map(destShow => destShow.id).includes(dest.id)).map(destination => {
        return {
          id: destination.id,
          isShowing: true,
        }
      })];
    },
    
    /**
     * Sets the rollback undo action.
     */
    undo() {
      const actions = [
        this.fetchAfterRollback
      ]; // fill;
      try {
        this.rollbackUndo(actions);
      } catch (err) {
        console.error(err);
      }
    },

    /**
     * Sets the redo rollback action.
     */
    redo() {
      const actions = [
        this.fetchAfterRollback
      ]; // fill;
      try {
        this.rollbackRedo(actions);
      } catch (err) {
        console.error(err);
      }
    }
  },

  computed: {
    /**
     * Checks if the user is authenticated to edit the selected destinatoin.
     */
    isAllowedToEdit() {
      return (
        this.isAdminUser ||
        this.focussedDestination.ownerId === this.userId
      );
    },

    visibleDestinations() {
      return this.isShowing.length ? this.destinations.filter(x => {
        const isShowing = this.isShowing.find(y => y.id === x.id);
        return isShowing ? isShowing.isShowing : false
      }) : this.destinations;
    }
  },

  async mounted() {
    this.isAdminUser = store.getters.getIsUserAdmin;
    this.userId = store.getters.getUser.id;
  },
};
</script>

