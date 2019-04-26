/* eslint-disable */

<template>
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
      <h2>Public destinations</h2>
      <li
        class="destination-list-element"
        v-for="item in destinations"
        :value="item.value"
        :key="item.value"
      >
        <div class="top-destination-content">
          <h2>{{ item.name }}</h2>
          <span>
            <!-- item.id -->
            <div v-if="isMyProfile" @click="editDestination(item.id)">
              <a>Edit</a>
            </div>
            <!--Sprint 3 todo<a v-on:click="deleteDestination(item.id)">Delete</a>-->
          </span>
        </div>
        <ul class="horizontal-details">
          <li>
            <p>
              <strong>COUNTRY:</strong>
              {{ item.country }}
            </p>
          </li>
          <li>
            <p>
              <strong>TYPE:</strong>
              {{ item.type }}
            </p>
          </li>
          <li>
            <p>
              <strong>DISTRICT:</strong>
              {{ item.district }}
            </p>
          </li>
          <li>
            <p>
              <strong>LATITUDE:</strong>
              {{ item.latitude }}
            </p>
          </li>
          <li>
            <p>
              <strong>LONGITUDE:</strong>
              {{ item.longitude }}
            </p>
          </li>
        </ul>
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

.horizontal-details {
  padding-top: 15px;
  background-color: #05386b;
}

.horizontal-details li {
  display: inline-block;
  padding-right: 20px;
  color: white;
}

.top-destination-content {
  background-color: #edf5e1;
  display: flex;
  justify-content: space-between;
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
import UserRepository from "../../repository/UserRepository";
import DestinationCreate from "./DestinationCreate"

export default {
  store,
  // local variables
  data() {
    return {
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
  methods: {
    editDestination(id) {
      this.$router.push("/user/"+this.user_id+"/destinations/edit/"+id);
    },
    toggleShowCreateDestination: function() {
      this.dialog = !this.dialog;
    },
    deleteDestination: function() {      
        // SPRINT 2 TODO
    },
    updateDestinationList: function()  {
      this.getDestinationList();
      this.dialog = false;
    },
    getDestinationList: function () {
      destinationRepository.getDestinations()
      .then(response => {
          this.destinations = response.data
      })
      .catch(err => {
          console.log(err)
    })
    },
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.user_id = id;
      this.isMyProfile = (store.getters.getUser.id);
    }
  },
  created: function() {
    this.checkIfProfileOwner();
    this.getDestinationList();
  }  
};
</script>
