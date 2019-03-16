/* eslint-disable */

<template>
  <v-container>
    <div v-if="!showCreateDestination">
      <v-btn class="button-min-width" flat @click="toggleShowCreateDestination">
        <v-icon dark left>keyboard_arrow_right</v-icon>Add new destination
      </v-btn>
      <destination-create
        v-bind:showAddDestination="showCreateDestination"
        v-bind:toggleShowCreateDestination="toggleShowCreateDestination"
      />
    </div>
    <div v-if="showCreateDestination">
      <v-btn class="button-min-width" flat @click="toggleShowCreateDestination">
        <v-icon dark left>keyboard_arrow_down</v-icon>Hide menu
      </v-btn>
      <destination-create
        v-bind:showAddDestination="showCreateDestination"
        v-bind:toggleShowCreateDestination="toggleShowCreateDestination"
      />
    </div>
    <ul>
      <h2>Public destinations</h2>
      <li
        class="destination-list-element"
        v-for="item in destination"
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
            <a v-on:click="deleteDestination(item.id)">Delete</a>
          </span>
        </div>
        <ul class="horizontal-details">
          <li>
            <p>
              <strong>COUNTRY:</strong>
              {{ item.id }}
            </p>
          </li>
          <li>
            <p>
              <strong>TYPE:</strong>
              {{ item.destination_type }}
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
              {{ item.crd_latitude }}
            </p>
          </li>
          <li>
            <p>
              <strong>LONGITUDE:</strong>
              {{ item.crd_longitude }}
            </p>
          </li>
        </ul>
      </li>
    </ul>
  </v-container>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.destination-container {
  display: flex;
}

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
import DestinationCreate from "./DestinationCreate.vue";
import {
  getDestination,
  deleteDestination
} from "../../repository/DestinationEditRepository";

export default {
  store,
  data() {
    return {
      showCreateDestination: false,
      showEditDestination: false,
      destination: store.getDestination
    };
  },
  components: {
    DestinationCreate: DestinationCreate
  },
  methods: {
    toggleShowCreateDestination: function() {
      this.showCreateDestination = !this.showCreateDestination;
    },
    deleteDestination: function(id) {
      for (var i = 0; i < this.destination.length; i++) {
        if (this.destination[i].id === id) {
          this.destination.splice(i, 1);
        }
      }
      deleteDestination(id).then(result => {
        console.log(result);
      });
    }
  },
  created: async function() {
    getDestination().then(result => {
      this.destination = result;
    });
  }
};
</script>
