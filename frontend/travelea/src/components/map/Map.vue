<template>
  <div class="map-container">

    <!-- this will be useful next sprint, the code works but the API key does not support this at the moment-->
    <div class="overlayed">
      <v-label>
        <GmapAutocomplete
          placeholder="Search"
          @place_changed="usePlace"
          class="v-text-field search-box"
          :select-first-on-enter="true"
          @keydown.native.enter.prevent
        ></GmapAutocomplete>
        <button class="search-btn" @click="usePlace">
          <i aria-hidden="true" class="v-icon material-icons">search</i>
        </button>
      </v-label>
    </div>

    <GmapMap class="main-map" :zoom="2" :center="{lat: 0, lng: 0}" ref="map" :options="{mapTypeControl: false}" @click="onMapClick($event)">
      <GmapMarker
        v-for="(marker, index) in destinationMarkers"
        :key="index"
        :position="{lat: marker.latitude, lng: marker.longitude}"
        :clickable="true"
        :draggable="true"
        @click="openInfoWindowTemplate(destinationMarkers[index])"
        @dragend="updateCoordinatesAfterDrag($event, index)"
        :icon="chooseIconForMarker(marker, index)"
      />
      <GmapInfoWindow
        :options="{maxWidth: 500}"
        :position="infoWindow.position"
        :opened="infoWindow.open"
        @closeclick="closeInfoWindow()"
      >
        <div v-if="selectedDest !== null">
          <div v-if="selectedDest.hasOwnProperty('id')">
            <h2>Country: {{this.selectedDest.country}}</h2>
            <h2>District: {{this.selectedDest.district}}</h2>
            <h2>Name: {{this.selectedDest.name}}</h2>
            <h2>Type: {{this.selectedDest.type}}</h2>
            <v-btn color="orange darken-2" dark v-on:click="navigateToDestination(selectedDest.id)">
              Visit
              <v-icon dark right>arrow_forward</v-icon>
            </v-btn>
          </div>
          <div v-else>
            <destination-create :createDestinationCallback="createDestinationCallback" :prefillData="{...selectedDest}"/>
          </div>
        </div>
      </GmapInfoWindow>
    </GmapMap>
  </div>
</template>

<style>
  .search-box {
    margin-left: 15px;
    margin-top: 15px;
    padding: 12px;
    padding-right: 40px;
    background-color: whitesmoke;
    border-radius: 4px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    width: 300px;
  }
  .search-btn {
    margin-left: -32px;
    padding: 0;
    cursor: pointer;
  }
  .overlayed {
    position: absolute;
    z-index: 2;
  }
  .map-container {
    width: 100%;
    height: 780px;
  }
  .main-map {
    width: 100%;
    height: 100%;
    position: absolute;
  }
</style>

<script>
import DestinationCreate from "../destination/DestinationCreate";
const pinkMarker = require("../../assets/pink-google-maps-marker.svg");
const blueMarker = require("../../assets/blue-google-maps-marker.svg");
const purpleMarker = require("../../assets/purple-google-maps-marker.svg");

import GoogleMapSmoothZoom from "../../plugins/google-map-smooth-zoom"

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
    destinationMarkers: Array,
    createDestinationCallback: Function,
  },
  watch: {},
  components: {DestinationCreate},
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
     * Determines whether the map marker should be pink or blue depending if it is public or private, or purple
     * if privacy is undefined.
     * @param marker
     */
    chooseIconForMarker(marker) {
      if (marker.isPublic === true) {
        return blueMarker;
      } else if (marker.isPublic === false) {
        return pinkMarker;
      } else {
        return purpleMarker;
      }
    },

    /*
     * Gets the selected place from the Google Maps autocomplete and adds it to destinations. After this,
     * it pans and zooms to the selected place's location
     * @param place The currently selected place
     */
    usePlace(place) {
      const zoomer = new GoogleMapSmoothZoom(this.$refs.map.$mapObject);
      this.place = place;

      console.log(place)

      if (!this.place.geometry) {
        return;
      }

      if (this.place) {
        let dataFormat = {
          latitude: this.place.geometry.location.lat(),
          longitude: this.place.geometry.location.lng(),
          temp: true
        };

        if (this.place.name) {
          dataFormat.name = this.place.name;
        }

        if (this.place.address_components) {
          dataFormat.country = (this.place.address_components.filter(x => {
            return x.types.includes("country")
          })[0].long_name);

          dataFormat.district = (this.place.address_components.filter(x => {
            if (x.types.includes("administrative_area_level_1")) {
              return x.types.includes("administrative_area_level_1")
            }
          })[0].long_name);
        }

        this.destinationMarkers.push(dataFormat);

        // Zoom out -> Pan to marker -> Zoom in to marker
        zoomer.out(10).then( () => {
          this.$refs.map.$mapObject.panTo({
            lat: this.place.geometry.location.lat(),
            lng: this.place.geometry.location.lng()
          });

          this.$refs.map.$mapObject.panTo({
            lat: this.place.geometry.location.lat(),
            lng: this.place.geometry.location.lng()
          });
          zoomer.in(12);
        });
      }
    },

    /*
     * Updates the location of the currently selected marker after a user stops dragging it.
     */
    updateCoordinatesAfterDrag(location, destinationMarkerIndex) {
      this.destinationMarkers[destinationMarkerIndex] = {
        latitude: parseFloat(location.latLng.lat()), longitude: parseFloat(location.latLng.lng())
      };
    },

    /*
     * Perceives a click on the map and creates a destination at the click location
     */
    onMapClick(clickEvent) {
      console.log(clickEvent);
      this.usePlace({
        geometry: {
          location: {
            lat: clickEvent.latLng.lat,
            lng: clickEvent.latLng.lng
          }
        },
      });
    }
  },
  created() {}
};
</script>