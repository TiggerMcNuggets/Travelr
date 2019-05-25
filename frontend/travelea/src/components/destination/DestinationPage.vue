<template>
    <v-layout row wrap>
        <!-- Main Nav -->
        <v-flex xs12> 
          <v-card>
            <v-layout pa-2 align-center>
              <h3>Destinations</h3>
              <v-spacer></v-spacer>
              <v-layout justify-end align-center>
                 Undo/Redo Here
                 <v-btn>Create Destination</v-btn>
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
              <DestinationMap :destinations="visableDestinations" :focussedDestination="{...focussedDestination}" :focusDestination="focusDestination"></DestinationMap>
            </v-flex>
            <v-flex xs4 sm3 md2 v-if="focussedDestination.data">
              <DestinationDetails v-if="focussedDestination.data" :destination="{...focussedDestination}" :passBackDestination="submitDestination"></DestinationDetails>
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
      newDestination: false
    };
  },


  watch: {      
  },

  computed: {
    
  },

  methods: {
    toggleDestination(destination) {

      var showDest = this.destinations.filter(x => x.data.id === destination.data.id)
      
      if(showDest[0]) {
        showDest[0].isShowing = !showDest[0].isShowing;
      }

      console.log("Toggled");
      console.log(showDest[0])
    },
    focusDestination(destination) {
      this.focussedDestination = destination;

      console.log("Focused");
      console.log(this.focussedDestination);

    },
    submitDestination(destination) {
      if(destination.id) {
        DestinationRepository.updateDestination(this.userId, this.destination.id, this.destination).then(() => {
            this.populateDestinations();
            this.focusDestination = {};
        })
        .catch(err => {
          console.log(err);
        })
      } else {
        DestinationRepository.createDestination(this.userId, destination).then(() => {
            this.populateDestinations();
            this.focusDestination = {};
        })
        .catch(err => {
          console.log(err);
        })
      }
    },
    populateDestinations() { 
      DestinationRepository.getDestinations(this.userId).then(response => {
        response.data.forEach(data => {
          var destinationObject = {
            data: data,
            isShowing: true
          }

          this.destinations.push(destinationObject);
        })        
      })
      .catch(err => {
          console.log(err);
        })
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

