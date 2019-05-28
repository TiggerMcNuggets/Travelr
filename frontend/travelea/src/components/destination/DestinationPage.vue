<template>
  <v-layout row wrap>
    <!-- Main Nav -->
    <v-flex xs12> 
          <v-card>
            <v-layout pa-2 align-center>
              <h3>Destinations</h3>
              <v-spacer></v-spacer>
              <v-layout justify-end align-center>
                  <div v-if="focussedDestination.data && focussedDestination.data.id">
                      <UndoRedoButtons
                              :canRedo="rollbackCanRedo()"
                              :canUndo="rollbackCanUndo()"
                              :undo="undo"
                              :redo="redo"></UndoRedoButtons>
                  </div>
              </v-layout>
             
            </v-layout>            
          </v-card>          
    </v-flex>

    <v-flex xs12>
      <v-layout row>
        <v-card v-if="!browseActive">
          <v-flex pa-3 class="menu-icon">
            <v-icon @click="browseActive = true">view_list</v-icon>
          </v-flex>
        </v-card>
        <v-flex v-else xs4 sm3 md2>
          <DestinationNav
            :destinations="destinations"
            :focusDestination="focusDestination"
            :toggleDestination="toggleDestination"
            :closeDestinationNav="() => { browseActive = false }"
          ></DestinationNav>
        </v-flex>
            <v-flex>
              <DestinationMap :destinations="visableDestinations" :focussedDestination="deepCopy(focussedDestination)" :focusDestination="focusDestination"></DestinationMap>
            </v-flex>

            <v-flex xs4 sm3 md2 v-if="focussedDestination.data">
              <DestinationDetails 
                v-if="focussedDestination.data" 
                :focussedDestination="deepCopy(focussedDestination)" 
                :passBackDestination="submitDestination"
                :focusDestination="focusDestination"
                :cancelEdit="cancelEdit"
                ></DestinationDetails>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<style>
.menu-item:hover {
  background-color: rgba(0, 0, 0, 0.87);
}

.destination-side-nav {
  width: 200px;
}
</style>


<script>
import { store } from "../../store/index";
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let DestinationRepository = RepositoryFactory.get("destination");
import RollbackMixin from "../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons.vue";
import DestinationNav from "./DestinationNav"
import DestinationMap from "./DestinationMap"
import DestinationDetails from "./DestinationDetails"
import {deepCopy} from "../../tools/deepCopy"

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
      userId: store.getters.getUser.id
    };
  },

  watch: {
    /**
     * Reset back to viewing mode on destination change
     */
    focussedDestination: {
      handler: function(newValue, oldValue) {
        if(oldValue.data.id !== newValue.data.id) {
          this.rollbackFlush();
        }
      }, 
      deep: true
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
    toggleDestination(destination) {
      var showDest = this.destinations.filter(
        x => x.data.id === destination.data.id
      );

      if (showDest[0]) {
        showDest[0].isShowing = !showDest[0].isShowing;
      }

    },
    focusDestination(destination) {
      this.focussedDestination = this.deepCopy(destination); 
      if (this.focussedDestination.data && this.focussedDestination.data.id) {
        const newDest = this.destinations.filter((dest) => dest.data.id === this.focussedDestination.data.id)[0];
        console.log(newDest);
        this.rollbackSetPreviousBody(newDest.data);
      }
    },
    submitDestination(destination) {
      if(destination.data.id) {    
        DestinationRepository.updateDestination(this.userId, destination.data.id, destination.data).then(() => {
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
        })
      } else {
        DestinationRepository.createDestination(this.userId, destination.data)
          .then(() => {
            this.populateDestinations();
            this.focussedDestination = {};
        })
        .catch(err => {
          console.error(err);
        })
      }
    },
    async populateDestinations() { 
      this.destinations = [];
      try {
        const response = await DestinationRepository.getDestinations(this.userId);
        response.data.forEach(data => {
          var destinationObject = {
            data: data,
            isShowing: true
          };
          this.destinations.push(destinationObject);
        })        
      } catch(err) {
          console.error(err);
        }
    },
    cancelEdit() {
      this.focussedDestination = {};
    },
    async fetchAfterRollback() {
      let id = this.focussedDestination.data.id;
      console.log(id);
      await this.populateDestinations();
      this.focussedDestination = this.destinations.filter((dest) => dest.data.id === id)[0];
      console.log(this.focussedDestination);
    },
    undo() {
        const actions = [this.fetchAfterRollback];// fill;
        try {
            this.rollbackUndo(actions);
        } catch (err) {
            console.error(err);
        }
    },
    redo() {
        const actions = [this.fetchAfterRollback];// fill;
        try {
            this.rollbackRedo(actions);
        } catch (err) {
            console.error(err);

        }
    }
  },
  computed: {
    visableDestinations() {
      return this.destinations.filter(x => x.isShowing);
    }
  },

  created() {
    this.populateDestinations();
  }
};
</script>

