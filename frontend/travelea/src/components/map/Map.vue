<template>
  <div class="map-container">
    <!--// this will be useful next sprint, the code works but the API key does not support this at the moment-->
    <!--<h1>Autocomplete Example (#164)</h1>-->
    <!--<label>-->
    <!--AutoComplete-->
    <!--<GmapAutocomplete-->
    <!--placeholder="This is a placeholder text"-->
    <!--@place_changed="setPlace">-->
    <!--</GmapAutocomplete>-->
    <!--<button @click="usePlace">Add</button>-->
    <!--</label>-->
    <!--<br/>-->

    <GmapMap class="main-map" :zoom="2" :center="{lat: 0, lng: 0}" ref="map">
      <GmapMarker
        v-for="(marker, index) in destinationsMarkers"
        :key="index"
        :position="{lat: marker.latitude, lng: marker.longitude}"
        :clickable="true"
        @click="openInfoWindowTemplate(destinationsMarkers[index])"
        :icon="chooseIconForMarker(marker, index)"
      />
      <GmapInfoWindow
        :options="{maxWidth: 300}"
        :position="infoWindow.position"
        :opened="infoWindow.open"
        @closeclick="closeInfoWindow()"
      >
        <div v-if="selectedDest !== null">
          <div>
            <h2>Country: {{this.selectedDest.country}}</h2>
            <h2>District: {{this.selectedDest.district}}</h2>
            <h2>Name: {{this.selectedDest.name}}</h2>
            <h2>Type: {{this.selectedDest.type}}</h2>
            <v-btn color="orange darken-2" dark v-on:click="navigateToDestination(selectedDest.id)">
              Visit
              <v-icon dark right>arrow_forward</v-icon>
            </v-btn>
          </div>
        </div>
      </GmapInfoWindow>
    </GmapMap>
  </div>
</template>

<style>
.map-container {
  width: 100%;
  height: 800px;
}
.main-map {
  width: 100%;
  height: 100%;
}
</style>

<script>
const pinkMarker = require("../../assets/pink-google-maps-marker.svg");
const blueMarker = require("../../assets/blue-google-maps-marker.svg");
export default {
  data() {
    return {
      userId: this.$route.params.user_id
        ? this.$route.params.user_id
        : this.$route.params.id,
      infoWindow: {
        position: { lat: 0, lng: 0 },
        open: false
      },
      selectedDest: null,
      place: null
    };
  },
  props: {
    destinationsMarkers: Array
  },
  watch: {},
  components: {},
  methods: {
    /**
     * Opens up the small information window for the particular map icon which is clicked on.
     * @param item The destination item being clicked on.
     */
    openInfoWindowTemplate(item) {
      this.infoWindow.position = { lat: item.latitude, lng: item.longitude };
      this.infoWindow.open = true;
      this.selectedDest = item;
    },

    /**
     * Closes the information window for the clicked destination.
     */
    closeInfoWindow() {
      this.infoWindow.open = false;
      this.selectedDest = null;
    },

    /**
     * Logs the event to the window.
     * @param evt The event to log.
     */
    log: function(evt) {
      window.console.log(evt);
    },

    /***
     * Redirects the user to the single destination page based on the destination which was clicked on.
     * @param destId
     */
    navigateToDestination: function(destId) {
      this.$router.push(`/user/${this.userId}/destinations/${destId}`);
    },

    /**
     * Determines whether the map marker should be pink or blue depending if it is public or private.
     * @param marker
     */
    chooseIconForMarker(marker) {
      if (marker.isPublic) {
        return blueMarker;
      } else {
        return pinkMarker;
      }
    }

    // this code will be needed for next sprint

    // setPlace(place) {
    //     this.place = place
    // },
    // usePlace() {
    //     if (this.place) {
    //         this.markers.push({
    //             position: {
    //                 lat: this.place.geometry.location.lat(),
    //                 lng: this.place.geometry.location.lng(),
    //             }
    //         });
    //         this.place = null;
    //     }
    // }

    //
  },
  created() {}
};
</script>