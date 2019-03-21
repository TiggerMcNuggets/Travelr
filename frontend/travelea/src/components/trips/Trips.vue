/* eslint-disable */

<template>
  <v-container>
    <div v-if="!showCreateTrip">
      <v-btn class="button-min-width" flat @click="toggleShowCreateTrip">
        <v-icon dark left>keyboard_arrow_right</v-icon>Add new trip
      </v-btn>
      <create-trip
        v-if="showCreateTrip"
        v-bind:toggleShowCreateTrip="toggleShowCreateTrip"
      />
    </div>
    <div v-if="showCreateTrip">
      <v-btn class="button-min-width" flat @click="toggleShowCreateTrip">
        <v-icon dark left>keyboard_arrow_down</v-icon>Hide menu
      </v-btn>
      <create-trip
        v-if="showCreateTrip"
        v-bind:toggleShowCreateTrip="toggleShowCreateTrip"
      />
    </div>
    <ul>
      <h2>My Trips</h2>
      <v-container
        id="input-usage"
        md6>
        <v-input
          :messages="['Messages']"
          append-icon="close"
          prepend-icon="phone"
        >
          Default Slot
        </v-input>
  </v-container>
      <li
        class="trips-list-element"
        v-for="item in trips"
        :value="item.value"
        :key="item.value"
      >
        <div class="top-destination-content">
          <h2>{{ item.name }}</h2>
          <span>
            <!-- item.id -->
            <router-link :to="{name: 'edit-destination', params: {id: item.id}}">
              <a>Edit</a>
            </router-link>
            <a v-on:click="deleteTrip(item.id)">Delete</a>
          </span>
        </div>
      </li>
    </ul>
  </v-container>
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

.trips-list-element {
  padding-top: 20px;
}
</style>


<script>
import { store } from "../../store/index";
import CreateTrips from "./CreateTrips.vue";
import { deleteTripById } from "../../repository/TripRepository";

export default {
  store,
  // local variables
  data() {
    return {
      showCreateTrip: false,
    //   showEditDestination: false
    };
  },
  // the place where you want to make the store values readable
  computed: {
    trips() {
      return store.state.trips.trips;
    }
  },
  // child components
  components: {
    CreateTrip: CreateTrips
  },
  methods: {
    toggleShowCreateTrip: function() {
      this.showCreateTrip = !this.showCreateTrip;
    },
    deleteTrip: function(id) {
      deleteTripById(id).then(result => {
        console.log(result);
        store.commit("setTrips", 0);
      });
    }
  },
  created: async function() {
    // committing to the store like this allows you to trigger the setDestinations mutation you can find in the destinations module for the store
    store.commit("setTrips", 0);
  }
};
</script>
