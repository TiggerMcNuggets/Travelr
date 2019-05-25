/* eslint-disable */

<template>
  <div class="destination-map">
    <Map
      :destinationMarkers="this.destinationMarkers"
      :createDestinationCallback="createDestinationCallback"
      :editDestinationCallback="editDestinationCallback"
    />
    <MapMenu
      :destinations="this.destinations"
      :updateDestinationsMarkers="this.updateDestinationsMarkers"
    />
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

ul {
  padding-left: 0px;
}

h2 {
  align-self: flex-end;
}

hr {
  margin-bottom: 25px;
}
ul {
  list-style: none;
}

.destination-map {
  display: flex;
  flex-direction: row;
  align-content: center;
  height: 800px;
}
</style>

<script>
import { store } from "../../store/index";
import Map from "../map/Map";
import MapMenu from "../map/MapMenu";

export default {
  store,

  data() {
    return {
      destinationMarkers: []
    };
  },

  props: {
    destinations: Array,
    createDestinationCallback: Function,
    editDestinationCallback: Function
  },

  components: {
    Map: Map,
    MapMenu
  },

  methods: {
    /**
     * Updates the markers if based on a list of destinations.
     * @param listDest
     */
    updateDestinationsMarkers(listDest) {
      this.destinationMarkers = listDest.map((dest) => {
        return {...dest, temp: false}
      });
    }
  },
  created() {}
};
</script>
