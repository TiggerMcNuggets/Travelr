<template>
  <v-layout>
    <div class="destination-overlayed">
            <v-layout row align-center ma-3>
        <v-btn @click="openDestinationNav" small fab color="error">
          <v-icon>view_list</v-icon>
        </v-btn>
  
        <v-btn fab small @click="toggleDarkMode" color="error">
          <v-icon>brightness_2</v-icon>
        </v-btn>
   

     
        <GmapAutocomplete
          placeholder="Search"
          @place_changed="onSearch"
          class="v-text-field destination-search-box"
          :select-first-on-enter="true"
          @keydown.native.enter.prevent
        ></GmapAutocomplete>
      
          <v-icon class="destination-search-btn">search</v-icon>
      
      </v-layout>
    </div>
    <v-flex class="map-flex">
      <GmapMap
        class="destination-main-map"
        :center="gMapOptions.center"
        ref="map"
        :options="gMapOptions"
        @click="onMapClick"
      >
        <!-- Private destination markers -->
        <GmapMarker
          v-for="destination in privateDestinations"
          :key="destination.id"
          :position="{lat: destination.latitude, lng: destination.longitude}"
          :draggable="false"
          :clickable="true"
          @click="focusDestination(destination)"
          :icon="privateMarker"
        />

        <!-- Public destination markers -->
        <GmapMarker
          v-for="destination in publicDestinations"
          :key="destination.id"
          :position="{lat: destination.latitude, lng: destination.longitude}"
          :draggable="false"
          :clickable="true"
          @click="focusDestination(destination)"
          :icon="publicMarker"
        />

        <!-- Focussed destination marker -->
        <GmapMarker
          v-if="focussedDestination.latitude && focussedDestination.longitude"
          :position="{lat: focussedDestination.latitude, lng: focussedDestination.longitude}"
          :draggable="false"
          :clickable="true"
        />
      </GmapMap>
    </v-flex>
  </v-layout>
</template>

<style>
.destination-side-nav {
  width: 200px;
}
.destination-search-box {
  margin-top: 0px;
  padding: 12px;
  padding-right: 40px; 
  vertical-align: center;
  margin-left: 20px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
  width: 300px;
}
.destination-search-btn {
  margin-left: -32px;
  padding: 0;
}
.destination-overlayed {
  position: absolute;
  z-index: 2;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.destination-darkmode-switch {
  margin-left: 330px;
  margin-top: 10px;
}
.destination-main-map {
  width: 100%;
}
</style>

<script>
import GoogleMapSmoothZoom from "../../plugins/google-map-smooth-zoom";
import { GoogleMapLightStyle } from "../../assets/google-map-light-style";
import { GoogleMapDarkStyle } from "../../assets/google-map-dark-style";

import { toTitleCase } from "../../tools/google_maps/googleMapsUtils";

// resources
import pinkMarker from "../../assets/pink-google-maps-marker.svg";
import blueMarker from "../../assets/blue-google-maps-marker.svg";

  export default {
    data() {
      return {
        gMapOptions: {
          mapTypeControl: false,
          styles: GoogleMapLightStyle,
          maxZoom: 18,
          minZoom: 3,
          zoom: 3,
          streetViewControl: false,
          fullscreenControl: false,
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
        privateMarker: pinkMarker
      };
    },

    props: {
      destinations: Array,
      focusDestination: Function,
      focussedDestination: Object,
      openDestinationNav: Function
    },

    computed: {

      /*
       * Returns the list of private destinations
       */
      privateDestinations() {
        return this.destinations.filter(x => !x.isPublic && x.id !== this.focussedId);
      },

      /*
       * Returns the list of public destinations
       */
      publicDestinations() {
        return this.destinations.filter(x => x.isPublic && x.id !== this.focussedId);
      },


      /*
       * Returns -1 to avoid indexing an undefined object.
       */
      focussedId() {
        if (this.focussedDestination != null) {
          return this.focussedDestination.id;
        }
        return -1;
      },
  },

  methods: {
    /*
     * Toggles the map to have a dark theme
     */
    toggleDarkMode() {
      if (this.darkModeOn) {
        this.gMapOptions.styles = GoogleMapLightStyle;
      } else {
        this.gMapOptions.styles = GoogleMapDarkStyle;
      }
      this.darkModeOn = !this.darkModeOn;
    },

    /*
     * Whenever coordinates are supplied, pan and zoom to them.
     */
    panAndZoom(coordinates) {
      const zoomer = new GoogleMapSmoothZoom(this.$refs.map.$mapObject);

      // Zoom out -> Pan to marker -> Zoom in to marker
      zoomer.out(11).then(() => {
        this.$refs.map.$mapObject.panTo({
          lat: coordinates.latitude,
          lng: coordinates.longitude
        });
        zoomer.in(15);
      });
    },
      /*
       * Updates the location of the currently selected marker after a user stops dragging it.
       */
      updateCoordinatesAfterDrag(location, focussedDestination) {
        this.focussedDestination.latitude = location.latLng.lat();
        this.focussedDestination.longitude = location.latLng.lng();
        this.focusDestination(focussedDestination);
      },

    /*
     * Updates the selected destination details in the info panel with autocomplete search details
     */
    onSearch(searchData) {
      if (searchData) {
        let destinationData = {
          latitude: searchData.geometry.location.lat(),
          longitude: searchData.geometry.location.lng()
        };
        let coordinates = {
          latitude: destinationData.latitude,
          longitude: destinationData.longitude
        };
        console.log(searchData);

        if (searchData.name) {
          destinationData.name = searchData.name;
        }

        if (searchData.types) {
          let containsLocality = searchData.types.filter(x => {
            return x === "locality";
          })[0];

          if (containsLocality) {
            destinationData.type = "City";
          } else {
            destinationData.type = toTitleCase(searchData.types[0]);
          }
        }

        if (searchData.address_components) {
          destinationData.country = searchData.address_components.filter(x => {
            return x.types.includes("country");
          })[0].long_name;
          
          destinationData.district = searchData.address_components.filter(x => {
            return x.types.includes("administrative_area_level_1");
          })[0].long_name;
        }
        console.log(destinationData);
        this.focusDestination(destinationData);
        // this.placeNewMarker(coordinates);
        this.panAndZoom(coordinates);
      }
    },

      /*
       * Places a new marker on the map and updates the parameters on the destination info panel
       */
      placeNewMarker(coordinates) {
        let destination = {...this.focussedDestination};
        if (!this.focussedDestination) {
          destination = {};
        } else if (this.focussedDestination && this.focussedDestination.id) {
          destination = {};
        }

        destination.latitude = coordinates.latitude;
        destination.longitude = coordinates.longitude;
        this.focusDestination(destination);
      },
    //     this.focussedDestination.data = destinationData;
    //     this.placeNewMarker(coordinates);
    //     this.panAndZoom(coordinates);
    //   }
    // },

    // /*
    //  * Places a new marker on the map and updates the parameters on the destination info panel
    //  */
    // placeNewMarker(coordinates) {
    //   if (!this.focussedDestination.data) {
    //     this.focussedDestination.data = {};
    //   } else if (
    //     this.focussedDestination.data &&
    //     this.focussedDestination.data.id
    //   ) {
    //     this.focussedDestination.data = {};
    //   }

    //   this.focussedDestination.data.latitude = coordinates.latitude;
    //   this.focussedDestination.data.longitude = coordinates.longitude;
    //   this.focusDestination(this.focussedDestination);
    // },

      /*
       * Perceives a click on the map and creates a destination at the click location
       */
      onMapClick(clickEvent) {
        this.placeNewMarker({
          latitude: clickEvent.latLng.lat(),
          longitude: clickEvent.latLng.lng()
        });
      },


    /**
     * Updates the map height when the window is resized.
     */
    updateMapHeight() {
      let height = window.innerHeight - 64; // Window innerheight with header subtracted is map area.
      this.$refs.map.$el.style.height = `${height}px`; // Setting the map to the height calculated above.
    }
  },

  /**
   * Sets the map height to the right height initially and binds the window resize event to the function to resize the map.
   */
  mounted() {
    this.updateMapHeight();
    window.addEventListener("resize", this.updateMapHeight);
  },

  /**
   * Removes the event listener for the resize event from the component.
   */
  beforeDestroy: function() {
    window.removeEventListener("resize", this.updateMapHeight);
  }
};
</script>
