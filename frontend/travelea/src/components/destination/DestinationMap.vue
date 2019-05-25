<template>
  <v-layout>
    <div class="destination-overlayed">
      <v-label>
        <GmapAutocomplete
          placeholder="Search"
          @place_changed="onSearch"
          class="v-text-field destination-search-box"
          :select-first-on-enter="true"
          @keydown.native.enter.prevent
        ></GmapAutocomplete>
        <button class="destination-search-btn" @click="onSearch">
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

      /*
       *
       */
      privateDestinations() {
        return this.destinations.filter(x => !x.data.isPublic && x.data.id !== this.focussedId);
      },

      /*
       *
       */
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
       * Whenever coordinates are supplied, pan and zoom to them.
       */
      panAndZoom(coordinates) {
        const zoomer = new GoogleMapSmoothZoom(this.$refs.map.$mapObject);

        // Zoom out -> Pan to marker -> Zoom in to marker
        zoomer.out(11).then( () => {
          this.$refs.map.$mapObject.panTo({
            lat: coordinates.latitude,
            lng: coordinates.longitude
          });

          this.$refs.map.$mapObject.panTo({
            lat: coordinates.latitude,
            lng: coordinates.longitude
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

      /*
       *
       */
      onSearch(searchData) {
        if (searchData) {

          let destinationData = {
            latitude: searchData.geometry.location.lat(),
            longitude: searchData.geometry.location.lng(),
          };
          let coordinates = {latitude: destinationData.latitude, longitude: destinationData.longitude};

          if (searchData.name) {
            destinationData.name = searchData.name;
          }

          if (searchData.types) {
            let containsLocality = (searchData.types.filter(x => {
              return x === "locality"
            })[0]);

            if (containsLocality) {
              destinationData.type = "City"
            } else {
              destinationData.type = this.toTitleCase(searchData.types[0]);
            }
          }

          if (searchData.address_components) {
            destinationData.country = (searchData.address_components.filter(x => {
              return x.types.includes("country")
            })[0].long_name);

            destinationData.district = (searchData.address_components.filter(x => {
              return x.types.includes("administrative_area_level_1")
            })[0].long_name);
          }

          this.placeNewMarker(coordinates);
          this.panAndZoom(coordinates);

          this.focussedDestination.data = destinationData;
          this.focusDestination(this.focussedDestination);
        }
      },

      /*
       *
       */
      placeNewMarker(coordinates) {
        if (!this.focussedDestination.data) {
          this.focussedDestination.data = {};
        } else if (this.focussedDestination.data && this.focussedDestination.data.id) {
          this.focussedDestination.data = {};
        }

        this.focussedDestination.data.latitude = coordinates.latitude;
        this.focussedDestination.data.longitude = coordinates.longitude;
        this.focusDestination(this.focussedDestination);
      },

      /*
       *
       */
      toTitleCase(s) {
        return s
          .replace(/([^A-Z])([A-Z])/g, '$1 $2') // split cameCase
          .replace(/[_\-]+/g, ' ') // split snake_case and lisp-case
          .toLowerCase()
          .replace(/(^\w|\b\w)/g, function(m) { return m.toUpperCase(); }) // title case words
          .replace(/\s+/g, ' ') // collapse repeated whitespace
          .replace(/^\s+|\s+$/, ''); // remove leading/trailing whitespace
      },

      /*
       * Perceives a click on the map and creates a destination at the click location
       */
      onMapClick(clickEvent) {
        this.placeNewMarker({
          latitude: clickEvent.latLng.lat(),
          longitude: clickEvent.latLng.lng()
        });
      }
    }
  };
</script>
