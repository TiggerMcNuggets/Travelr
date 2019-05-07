/* eslint-disable */

<template>
  <v-container class="outer-container" height="100%" style="margin-left: 0px; margin-top: -20px;">
    <div class="section">
        <div class="dest-name">
          <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
            <v-icon dark>keyboard_arrow_left</v-icon>
          </v-btn>
    
          <h2 class="headline">Trips</h2>
  
        </div>
        <div>
          <v-btn
            class="upload-toggle-button"
            fab
            small
            dark
            color="indigo"
            v-if="isMyProfile || isAdminUser"
            @click="toggleShowCreateTrip"
          >
            <v-icon dark>add</v-icon>
          </v-btn>

          <v-btn
            class="upload-toggle-button"
            fab
            small
            dark
            color="indigo"
            @click="toggleShowSearch"
          >
            <v-icon dark>search</v-icon>
          </v-btn>
        </div>
      </div>

        <v-divider class="photo-header-divider"></v-divider>
      <v-text-field v-if="searchActive" v-model="searchValue" label="Trip name" prepend-icon="search"></v-text-field>
    
    <!-- <div v-if="this.isMyProfile || this.isAdmin">
      <div v-if="!showCreateTrip && (this.isMyProfile || this.isAdmin)">
        <v-btn class="button-min-width" flat @click="toggleShowCreateTrip">
          <v-icon dark left>keyboard_arrow_right</v-icon>Add new trip
        </v-btn>
        <create-trip
          v-if="showCreateTrip"
          v-bind:toggleShowCreateTrip="toggleShowCreateTrip"
        />
      </div> -->
      <div v-if="showCreateTrip">
        <!-- <v-btn class="button-min-width" flat @click="toggleShowCreateTrip">
          <v-icon dark left>keyboard_arrow_down</v-icon>Hide menu
        </v-btn> -->
          <create-trip
          v-if="showCreateTrip"
          :toggleShowCreateTrip="toggleShowCreateTrip"
          :regetTrips="regetTrips"
          :passedTrip="null"
          :updateViewTripPage="() => console.log()"
          />
      </div>
    <!-- </div> -->

    <ul>
    
      <!-- <div class="input-field-right-margin">
        <v-text-field
                v-model="searchValue"
                label="Trip name"
                prepend-icon="search"
        ></v-text-field>
      </div> -->
      <li
        class="trips-list-element"
        v-for="item in tripsFiltered"
        :value="item.value"
        :key="item.value"
      >
        <v-card  v-on:click="openTrip(item.id)">
          <div class="top-destination-content">
            <h2>{{ item.name }}</h2>
          </div>
        </v-card>
      </li>
    </ul>
  </v-container>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.horizontal-details li {
  display: inline-block;
  padding-right: 20px;
  color: white;
}

.top-destination-content {
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

.trips-list-element {
  padding-top: 20px;
}

</style>


<script>
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";
import  { RepositoryFactory } from "../../repository/RepositoryFactory"
let tripRepository = RepositoryFactory.get("trip");

export default {
  store,
  // local variables
  data() {
    return {
      showCreateTrip: false,
      searchValue: "",
      trips: [],
        isAdmin: store.getters.getIsUserAdmin,
      isMyProfile: false,
      isAdminUser: false,
      user_id: this.$route.params.id,
      searchActive: false
    };
  },
  // the place where you want to make the store values readable
  computed: {

    tripsFiltered() {
      const filteredList = this.trips.filter(trip => trip.name.toLowerCase().search(this.searchValue.toLowerCase()) !== -1);
      //Currently sorting trips by id, in future we will sort trips by creation time
      return filteredList.sort(function(a, b){ return a.id - b.id; });
    }

  },
  // child components
  components: {
    CreateTrip: CreateTrips
  },
  methods: {
    getTrips: function() {
        tripRepository.getUserTrips(this.user_id)
        .then((res) => {
            this.trips = res.data;
        })
        .catch((err) => {
            console.log(err);
        })
    },
    toggleShowSearch: function() {
      this.searchActive = !this.searchActive;
    },
    getUserTrips: function() {
        tripRepository.getUserTrips(this.user_id)
        .then((res) => {
            this.trips = res.data;
        })
        .catch((err) => {
            console.log(err);
        })
    },

    openTrip: function(id) {
      let route = `/user/${this.user_id}/trips/`;
      if (this.isMyProfile || store.getters.getIsUserAdmin) {
        route = `/user/${this.user_id}/trips/${id}`
      }
      this.$router.push(route);
    },

    toggleShowCreateTrip: function() {
      this.showCreateTrip = !this.showCreateTrip;
    },

    regetTrips: function() {
      this.toggleShowCreateTrip();
      if (this.isMyProfile) {
        this.getTrips();
      } else {
        this.getUserTrips();
      }
    },

    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.isMyProfile = (store.getters.getUser.id == id);
    }
  },
  created: function() {
    this.checkIfProfileOwner();
    this.isAdminUser = store.getters.getIsUserAdmin;
    if (this.isMyProfile) {
      this.getTrips();
    } else {
      this.getUserTrips();
    }
  }
};
</script>