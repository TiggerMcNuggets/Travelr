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
        <!-- Destinations Body -->
        <v-flex xs12> 
          <v-layout row>
            <v-flex xs4 sm3 md2>
              <DestinationNav :destinations="destinations" 
                              :focusDestination="focusDestination"
                              :toggleDestination="toggleDestination"
                              >
              </DestinationNav>
            </v-flex>
            <v-flex>
              <DestinationMap :destinations="visableDestinations" :focussedDestination="ballsDeep(focussedDestination)" :focusDestination="focusDestination"></DestinationMap>
            </v-flex>

            <v-flex xs4 sm3 md2 v-if="focussedDestination.data">
              <DestinationDetails 
                v-if="focussedDestination.data" 
                :focussedDestination="ballsDeep(focussedDestination)" 
                :passBackDestination="submitDestination"
                :focusDestination="focusDestination"
                :cancelEdit="cancelEdit"
                ></DestinationDetails>
            </v-flex>
          </v-layout>
        </v-flex>
    </v-layout>
</template>

<style>
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
      userId: store.getters.getUser.id,
    };
  },

  watch: {
  },

  methods: {
    createDestination() {
      this.focussedDestination = {
        data: {
        }
      }
    },
    toggleDestination(destination) {

      var showDest = this.destinations.filter(x => x.data.id === destination.data.id)
      
      if(showDest[0]) {
        showDest[0].isShowing = !showDest[0].isShowing;
      }

      console.log("Toggled");
      console.log(showDest[0])
    },
    focusDestination(destination) {
      this.focussedDestination = this.ballsDeep(destination);
      console.log('balls deep destination', destination.data);
      const deep = this.ballsDeep(destination);
    if(deep.data.travellerTypes.length != 0 && deep.data.travellerTypes[0].id != undefined) {
        var newList = []
        deep.data.travellerTypes.forEach(x => {
            newList.push(x.id);
        });
        deep.data.travellerTypes = newList;
    }

      this.rollbackSetPreviousBody(deep.data);

      console.log("Focused");
      console.log(this.focussedDestination);

    },
    submitDestination(destination) {
      if(destination.data.id) {
        
        if(destination.data.travellerTypes.length != 0 && destination.data.travellerTypes[0].id != undefined) {
          var newList = []
          destination.data.travellerTypes.forEach(x => {
            newList.push(x.id);
          });

          destination.data.travellerTypes = newList;
        }

        DestinationRepository.updateDestination(this.userId, destination.data.id, destination.data).then(() => {
            const url = `/users/${this.userId}/destinations/${destination.data.id}`;
            this.rollbackCheckpoint(
                'PUT',
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
            console.log('previous body', this.rollbackPreviousBody);
            console.log(this.rollbackCanUndo());
            this.populateDestinations();
            // this.focussedDestination = {};
        })
        .catch(err => {
          console.log(err);
        })
      } else {
        DestinationRepository.createDestination(this.userId, destination.data).then(() => {
            this.populateDestinations();
            this.focussedDestination = {};
        })
        .catch(err => {
          console.log(err);
        })
      }
    },
    populateDestinations() { 
      this.destinations = [];
      DestinationRepository.getDestinations(this.userId).then(response => {
        response.data.forEach(data => {
          var destinationObject = {
            data: data,
            isShowing: true
          };

          this.destinations.push(destinationObject);
        })        
      })
      .catch(err => {
          console.log(err);
        })
    },
    cancelEdit() {
      this.focussedDestination = {};
    },
    ballsDeep(object) {
        return JSON.parse(JSON.stringify(object));
    },
    undo() {
        const actions = [];// fill];
        try {
            this.rollbackUndo(actions);
        } catch (err) {
            console.error(err);
        }
    },
    redo() {
        const actions = [];// fill];
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

