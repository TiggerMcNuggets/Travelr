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
      <div class="input-field-right-margin">
        <v-text-field
                v-model="searchValue"
                label="Trip name"
                prepend-icon="search"
        ></v-text-field>
      </div>
      <li
        class="trips-list-element"
        v-for="item in tripsFiltered"
        :value="item.value"
        :key="item.value"
      >
        <v-card>
          <div class="top-destination-content">
            <h2>{{ item.name }}</h2>
            <span>
              <!-- item.id -->
              <a v-on:click="deleteTrip(item.id)">Delete</a>
            </span>
          </div>
        </v-card>
      </li>
    </ul>
  </v-container>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");


.input-field-right-margin {
  margin-right: 80%;
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
      searchValue: "",
    };
  },
  // the place where you want to make the store values readable
  computed: {
    trips() {
      return store.state.trips.trips;
    },

    tripsFiltered() {
      console.log('here');
      return this.trips.filter(trip => trip.name.search(this.searchValue) !== -1);
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
      deleteTripById(id).then(function(result) {
        console.log(result);
        store.commit("setTrips");
      });
    }
  },
  created: async function() {
    // committing to the store like this allows you to trigger the setDestinations mutation you can find in the destinations module for the store
    store.commit("setTrips");
    store.commit("setDestinations");
  }
};
</script>
