<template>
  <v-layout>
    <div class="destination-overlayed">
      <v-label>
        <GmapAutocomplete
          placeholder="Search"
          @place_changed="yeet"
          class="v-text-field destination-search-box"
          :select-first-on-enter="true"
          @keydown.native.enter.prevent
        ></GmapAutocomplete>
        <button class="destination-search-btn" @click="yeet">
          <i aria-hidden="true" class="v-icon material-icons">search</i>
        </button>
      </v-label>
    </div>
    <v-flex class="map-flex">

      <GmapMap class="destination-main-map" :center="gMapOptions.center" ref="map" :options="gMapOptions" @click="onMapClick">

        <!-- Private destination markers -->
        <GmapMarker
          v-for="destination in privateDestinations"
          :key="destination.data.id"
          :position="{lat: destination.data.latitude, lng: destination.data.longitude}"
          :draggable="false"
          :clickable="true"
          @click="focusDestination(destination)"
          :icon="privateMarker"
        />

        <!-- Public destination markers -->
        <GmapMarker
          v-for="destination in publicDestinations"
          :key="destination.data.id"
          :position="{lat: destination.data.latitude, lng: destination.data.longitude}"
          :draggable="false"
          :clickable="true"
          @click="focusDestination(destination)"
          :icon="publicMarker"
        />

        <!-- Focussed destination marker -->
        <GmapMarker
          v-if="focussedDestination.data"
          :position="{lat: focussedDestination.data.latitude, lng: focussedDestination.data.longitude}"
          :draggable="false"
          :clickable="true"
        />
        <!--          @dragend="updateCoordinatesAfterDrag($event, focussedDestination)"-->
      </GmapMap>
    </v-flex>

  </v-layout>
</template>

<style>
  .destination-side-nav {
    width: 200px;
  }
  .destination-search-box {
    margin-left: 15px;
    margin-top: 15px;
    padding: 12px;
    padding-right: 40px;
    background-color: whitesmoke;
    border-radius: 4px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    width: 300px;
  }
  .destination-search-btn {
    margin-left: -32px;
    padding: 0;
    cursor: pointer;
  }
  .destination-overlayed {
    position: absolute;
    z-index: 2;
  }
  .destination-main-map {
    width: 100%;
    height: 830px;
  }
</style>

<script>
  const pinkMarker = require("../../assets/pink-google-maps-marker.svg");
  const blueMarker = require("../../assets/blue-google-maps-marker.svg");
  const purpleMarker = require("../../assets/purple-google-maps-marker.svg");

  import GoogleMapSmoothZoom from "../../plugins/google-map-smooth-zoom"
  import { GoogleMapStyle } from "../../assets/google-map-style"

  export default {
    data() {
      return {
        gMapOptions: {
          mapTypeControl: false,
          styles: GoogleMapStyle,
          maxZoom: 18,
          minZoom: 3,
          zoom: 3,
          streetViewControl: false,
          mapTypeId: 'roadmap',
          center: {lat: 0, lng: 120},
          restriction: {
            latLngBounds: {north: 85, south: -85, west: -180, east: 180},
            strictBounds: true
          },
        },
        infoWindow: {
          position: { lat: 0, lng: 0 },
          open: false
        },
        publicMarker: blueMarker,
        privateMarker: purpleMarker
      };
    },

    props: {
      destinations: Array,
      focusDestination: Function,
      focussedDestination: Object
    },

    computed: {
      privateDestinations() {
        return this.destinations.filter(x => !x.data.isPublic && x.data.id !== this.focussedId);
      },

      publicDestinations() {
        return this.destinations.filter(x => x.data.isPublic && x.data.id !== this.focussedId);
      },

      /*
       * Returns -1 to avoid indexing an undefined object.
       */
      focussedId() {
        if (this.focussedDestination.data != null) {
          return this.focussedDestination.data.id;
        }
        return -1;
      },
    },

    methods: {

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

      /**
       * Opens up the small information window for the particular map icon which is clicked on.
       * @param item The destination item being clicked on.
       */
      openInfoWindowTemplate(item) {
        const xOffset = 0.01;
        this.infoWindow.position = { lat: item.data.latitude + xOffset, lng: item.data.longitude };
        this.infoWindow.open = true;
        this.focussedDestination = item;
      },

      /**
       * Closes the information window for the clicked destination.
       */
      closeInfoWindow() {
        this.infoWindow.open = false;
        this.infoWindow.editMode = false;
        this.focussedDestination = null;
      },

      /*
       * Whenever a place is chosen, pan and zoom to it.
       */
      panAndZoom() {
        const zoomer = new GoogleMapSmoothZoom(this.$refs.map.$mapObject);

        // Zoom out -> Pan to marker -> Zoom in to marker
        zoomer.out(11).then( () => {
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
      },

      /*
       * Updates the location of the currently selected marker after a user stops dragging it.
       */
      updateCoordinatesAfterDrag(location, focussedDestination) {
        this.focussedDestination.data.latitude = location.latLng.lat();
        this.focussedDestination.data.longitude = location.latLng.lng();
        this.focusDestination(focussedDestination);
      },

      yeet() {
        console.log("yeeted")
      },

      onSearch(destination) {
        console.log(destination)
        this.focussedDestination.data = destination;

        if (!this.place.geometry) {
          return;
        }

        if (this.place) {
          let placeData = {
            latitude: this.place.geometry.location.lat(),
            longitude: this.place.geometry.location.lng(),
            temp: true
          };

          if (this.place.name) {
            placeData.name = this.place.name;
          }

          if (this.place.address_components) {
            placeData.country = (this.place.address_components.filter(x => {
              return x.types.includes("country")
            })[0].long_name);

            placeData.district = (this.place.address_components.filter(x => {
              if (x.types.includes("administrative_area_level_1")) {
                return x.types.includes("administrative_area_level_1")
              }
            })[0].long_name);
          }

          this.destinationMarkers.push(placeData);
          this.panAndZoom();
        }
      },

      placeNewMarker(coordinates) {
        if (!this.focussedDestination.data) {
          this.focussedDestination.data = {};
        } else if (this.focussedDestination.data && this.focussedDestination.data.id) {
          this.focussedDestination.data = {};
        }

        this.focussedDestination.data.latitude = coordinates.lat();
        this.focussedDestination.data.longitude = coordinates.lng();
        this.focusDestination(this.focussedDestination);
      },

      /*
       * Perceives a click on the map and creates a destination at the click location
       */
      onMapClick(clickEvent) {
        this.placeNewMarker({
          lat: clickEvent.latLng.lat,
          lng: clickEvent.latLng.lng
        });
      }
    }
  };
</script>
