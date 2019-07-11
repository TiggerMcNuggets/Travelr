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

        <v-flex xs4 sm3 md2 v-if="focussedDestination.id">
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
            v-if="focussedDestination.id"
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
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let DestinationRepository = RepositoryFactory.get("destination");
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
      handler: function(newDestination, oldDestination) {
        this.isShowing = this.populateIsShowing();
      }
    }

  },

  methods: {
    deepCopy(value) {
      return deepCopy(value);
    },

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

    focusDestination(destination) {
      this.focussedDestination = this.deepCopy(destination);
      if (this.focussedDestination && this.focussedDestination.id) {
        const newDest = this.destinations.filter(
          dest => dest.id === this.focussedDestination.id
        )[0];
        this.rollbackSetPreviousBody(newDest);
      }
    },

    submitDestination(destination) {
      if (destination.id) {
        DestinationRepository.updateDestination(
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
            this.getDestinations(this.userId);
          })
          .catch(err => {
            console.error(err);
          });
      } else {
        DestinationRepository.createDestination(this.userId, destination)
          .then(() => {
            this.getDestinations();
            this.focussedDestination = {};
          })
          .catch(err => {
            console.error(err);
          });
      }
    },

    cancelEdit() {
      this.focussedDestination = {};
    },

    async fetchAfterRollback() {
      let id = this.focussedDestination.id;
      await this.getDestinations(this.userId);
      this.focussedDestination = this.destinations.filter(
        dest => dest.id === id
      )[0];
    },

    populateIsShowing() {
      return [...this.isShowing, ...this.destinations.map(dest => {
        if (!this.isShowing.map(destShow => destShow.id).includes(dest.id)) {
          return {
            id: dest.id,
            isShowing: true,
          }
        }
      })]
    },
    
    undo() {
      const actions = [this.fetchAfterRollback]; // fill;
      try {
        this.rollbackUndo(actions);
      } catch (err) {
        console.error(err);
      }
    },
    redo() {
      const actions = [this.fetchAfterRollback]; // fill;
      try {
        this.rollbackRedo(actions);
      } catch (err) {
        console.error(err);
      }
    }
  },
  computed: {
    isAllowedToEdit() {
      return (
        this.isAdminUser ||
        this.focussedDestination.ownerId === this.userId
      );
    },

    visibleDestinations() {
      return this.destinations.filter(x => this.isShowing.find(y => y.id === x.id).isShowing);
    }
  },

  async mounted() {
    this.isAdminUser = store.getters.getIsUserAdmin;
    this.userId = store.getters.getUser.id;
  },
};
</script>

