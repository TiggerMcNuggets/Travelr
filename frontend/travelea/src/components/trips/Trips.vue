/* eslint-disable */

<template>
  <v-container style="margin-left: 0px;">
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
        <create-trip style="margin-left: -100px;"
        v-if="showCreateTrip"
        :toggleShowCreateTrip="toggleShowCreateTrip"
        :regetTrips="regetTrips"
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
import  { RepositoryFactory } from "../../repository/RepositoryFactory"
let tripRepository = RepositoryFactory.get("trip");

export default {
  store,
  // local variables
  data() {
    return {
      showCreateTrip: false,
      searchValue: "",
      trips: []
    };
  },
  // the place where you want to make the store values readable
  computed: {

    tripsFiltered() {
      const filteredList = this.trips.filter(trip => trip.name.search(this.searchValue) !== -1);
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
        tripRepository.getTrips()
        .then((res) => {
          //console.log(res.data);
            this.trips = res.data;
        })
        .catch((err) => {
            console.log(err);
        })
    },

    openTrip: function(id) {
        this.$router.push("/trips/view/"+id);//window.location.href = '/#/trips/view/'+id;
    },

    toggleShowCreateTrip: function() {
      this.showCreateTrip = !this.showCreateTrip;
    },

    regetTrips: function() {
      this.toggleShowCreateTrip();
      this.getTrips();
    }
  },
  created: function() {
    this.getTrips();
  }
};
</script>