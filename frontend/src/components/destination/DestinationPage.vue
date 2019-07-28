<template>
  <v-layout row wrap>
    <!-- Main Nav -->
    <v-flex xs12>
      <v-layout row>
        <v-flex v-if="browseActive" xs4 sm3 md2>
          <DestinationNav
            :destinations="destinations"
            :focusDestination="focusDestination"
            :toggleDestination="toggleDestination"
            :closeDestinationNav="() => { browseActive = false }"
          ></DestinationNav>
        </v-flex>
        <v-flex>
          <DestinationMap
            :destinations="visableDestinations"
            :focussedDestination="deepCopy(focussedDestination)"
            :focusDestination="focusDestination"
            :openDestinationNav="() => { browseActive = !browseActive }"
          ></DestinationMap>
        </v-flex>

        <v-flex xs4 sm3 md2 v-if="focussedDestination.data">
          <v-flex
            v-if="focussedDestination.data && focussedDestination.data.id && isAllowedToEdit"
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
            v-if="focussedDestination.data"
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

export default {
  store,
  mixins: [RollbackMixin],
  components: {
    UndoRedoButtons,
    DestinationNav,
    DestinationMap,
    DestinationDetails
  },

  data() {
    return {
      destinations: [],
      focussedDestination: {},
      browseActive: false,

      // priviledges data
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
        if (oldValue.data.id !== newValue.data.id) {
          this.rollbackFlush();
        }
      },
      deep: true
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

    /**
     * Toggles between destinations
     */
    toggleDestination(destination) {
      var showDest = this.destinations.filter(
        x => x.data.id === destination.data.id
      );

      if (showDest[0]) {
        showDest[0].isShowing = !showDest[0].isShowing;
      }
    },

    /**
     * Changed the focused destination to a given destination
     */
    focusDestination(destination) {
      this.focussedDestination = this.deepCopy(destination);
      if (this.focussedDestination.data && this.focussedDestination.data.id) {
        const newDest = this.destinations.filter(
          dest => dest.data.id === this.focussedDestination.data.id
        )[0];
        this.rollbackSetPreviousBody(newDest.data);
      }
    },

    /**
     * Checks if a destination exists if it does, updates it. If it doesn't, a new destination is created.
     */
    submitDestination(destination) {
      if (destination.data.id) {
        DestinationRepository.updateDestination(
          this.userId,
          destination.data.id,
          destination.data
        )
          .then(() => {
            const url = `/users/${this.userId}/destinations/${destination.data.id}`;
            this.rollbackCheckpoint(
              "PUT",
              {
                url: url,
                body: destination.data
              },
              {
                url: url,
                body: this.rollbackPreviousBody
              }
            );
            this.rollbackPreviousBody = destination.data;
            this.populateDestinations();
          })
          .catch(err => {
            console.error(err);
          });
      } else {
        DestinationRepository.createDestination(this.userId, destination.data)
          .then(() => {
            this.populateDestinations();
            this.focussedDestination = {};
          })
          .catch(err => {
            console.error(err);
          });
      }
    },

    /**
     * Populates the destinations
     */
    async populateDestinations() {
      this.destinations = [];
      try {
        const response = await DestinationRepository.getDestinations(
          this.userId
        );
        response.data.forEach(data => {
          var destinationObject = {
            data: data,
            isShowing: true
          };
          this.destinations.push(destinationObject);
        });
      } catch (err) {
        console.error(err);
      }
    },

    /**
     * Cancels an edit on a clicked destination.
     */
    cancelEdit() {
      this.focussedDestination = {};
    },

    /**
     * Updates destinations and focussed destination following a rollback action.
     */
    async fetchAfterRollback() {
      let id = this.focussedDestination.data.id;
      await this.populateDestinations();
      this.focussedDestination = this.destinations.filter(
        dest => dest.data.id === id
      )[0];
    },

    /**
     * Sets the rollback undo action.
     */
    undo() {
      const actions = [this.fetchAfterRollback]; // fill;
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
      const actions = [this.fetchAfterRollback]; // fill;
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
        this.focussedDestination.data.ownerId === this.userId
      );
    },

    /**
     * Gets all visable destinations?
     */
    visableDestinations() {
      return this.destinations.filter(x => x.isShowing);
    }
  },

  /**
   * Populates all the destinations and sets the user information on component creation.
   */
  created() {
    this.populateDestinations();
    this.isAdminUser = store.getters.getIsUserAdmin;
    this.userId = store.getters.getUser.id;
  }
};
</script>

