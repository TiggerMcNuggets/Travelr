/* eslint-disable */

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
<v-card>
  <v-container style="margin-left: 0px; margin-top: -20px;">
    
    <v-btn fab small dark color="indigo" @click="$router.go(-1)">
        <v-icon dark>keyboard_arrow_left</v-icon>
    </v-btn>
    <div v-if="isMyProfile">
      <v-btn class="button-min-width" flat @click="toggleShowCreateDestination">
        <v-icon dark left>keyboard_arrow_right</v-icon>Add new destination
      </v-btn>      
    </div>
    <ul>
      <h2>Destinations</h2>
      <li
        class="destination-list-element"
        v-for="item in destinations"
        :value="item.value"
        :key="item.value">
        <v-card class="top-destination-content destination-container">
          <div class="row-container">
            <div class="private-public-side-bar" v-bind:class="{ 'pink-background': item.isPublic, 'blue-background': !item.isPublic }">
            </div>
            <div class="hoverable" v-on:click="viewDestination(item.id)">
              <h2>{{item.name}} | {{item.country}} | {{item.district}}</h2>
              <div class="row-container">
                <h3>Lat: {{item.latitude}} | Lng: {{item.longitude}}</h3>
              </div>
              <div class="row-container">
                <h3>Type: {{item.type}}</h3>
              </div>
            </div>
          </div>
          <div v-if="isMyProfile">
            <v-btn icon @click="deleteDestination(item.id)">
              <v-icon color="red lighten-1">delete</v-icon>
            </v-btn>
            <v-btn icon @click="editDestination(item.id)">
              <v-icon color="orange lighten-1">edit</v-icon>
            </v-btn>
            <v-btn v-if="!item.isPublic" icon @click="makePublic(item.id)">
              <v-icon color="blue lighten-1">lock</v-icon>
            </v-btn>
            <v-btn v-if="item.isPublic" icon @click="() => console.log('clicked on open lock')">
              <v-icon color="hotpink lighten-1">lock_open</v-icon>
            </v-btn>
          </div>

            <!--Sprint 3 todo<a v-on:click="deleteDestination(item.id)">Delete</a>-->
        </v-card>
      </li>
    </ul>
    

    <v-dialog v-model="dialog" width="800">
       <destination-create :createDestinationCallback="updateDestinationList" />
    </v-dialog>
    
  </v-container>
  </v-card>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.private-public-side-bar {
  width: 10px;
  margin-right: 20px;
}
.blue-background {
  background-color: #0d47a1;
}
.pink-background {
  
}

.destination-container {
  padding: 20px;
}

.hoverable:hover {
  cursor: pointer;
}

.row-container {
  display: flex;
  flex-direction: row;
  margin-top: 10px;
}

.top-destination-content span {
  padding: 10px 20px;
}

.top-destination-content a {
  font-family: "Karla", sans-serif;
  font-size: 15px;
  margin-left: 10px;
}

ul {
  list-style: none;
}

.destination-list-element {
  padding-top: 20px;
}
</style>


<script>
import { store } from "../../store/index";
import {RepositoryFactory} from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import DestinationCreate from "./DestinationCreate"

export default {
  store,
  // local variables
  data() {
    return {
        showTooltip: false,
        dialog: false,
        showEditDestination: false,
        destinations: [],
        isMyProfile: false,
        user_id: null
    };
  }, 
   // child components
  components: {
    DestinationCreate: DestinationCreate
  },
  watch: {
    '$route.params.id': function() {
      this.init();
    }
  },
  methods: {
    init() {
      this.checkIfProfileOwner();
      this.getDestinationList();
    },
    editDestination(id) {
      this.$router.push("/user/"+this.user_id+"/destinations/edit/"+id);
    },
    viewDestination(id) {
      this.$router.push("/user/"+this.user_id+"/destinations/"+id);
    },
    toggleShowCreateDestination: function() {
      this.dialog = !this.dialog;
    },
    deleteDestination: function() {
        // TODO: ion progress
    },
    updateDestinationList: function()  {
      this.getDestinationList();
      this.dialog = false;
    },
    getDestinationList: function () {
      destinationRepository.getDestinations(this.user_id)
      .then(response => {
          this.destinations = response.data
      })
      .catch(err => {
          console.log(err)
    })
    },
    makePublic: function (destId) {
        destinationRepository.makePublic(destId)
            .then((res) => {
                console.log(res);
                this.init();
            })
            .catch((err) => {
                console.log(err);
            });
    },
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.user_id = id;
      this.isMyProfile = (store.getters.getUser.id);
    }
  },
  created: function() {
    this.init();
  }  
};
</script>
