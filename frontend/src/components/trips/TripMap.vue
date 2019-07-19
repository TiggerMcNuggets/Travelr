<template>
  <v-layout>
    <v-flex class="map-flex">
      <GmapMap
        class="destination-main-map"
        :center="gMapOptions.center"
        ref="map"
        :options="gMapOptions"
      >
        <!-- Private destination markers -->
        <GmapMarker
          v-for="destination in path"
          :key="destination.id"
          :position="{lat: destination.lat, lng: destination.lng}"
          :draggable="false"
          :icon="privateMarker"
        />
        <GmapPolyline 
          :path="path">
        </GmapPolyline>
      </GmapMap>
    </v-flex>
  </v-layout>
</template>

<style>
.destination-main-map {
  width: 100%;
  height: 830px;
}
</style>

<script>
  import { GoogleMapLightStyle } from "../../assets/google-map-light-style"
  import { GoogleMapDarkStyle } from "../../assets/google-map-dark-style"

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
        privateMarker: pinkMarker,
        path: [
            {lat: 55.9358131, lng: -4.7770143 },
            {lat: 55.9361256, lng: -4.7767353 },
            {lat: 55.9366784, lng: -4.7739458 }
],
      };
    },

    props: {
      destinations: Array
    },

    created() {
      console.log("test" + this.destinations);
    },

    computed: {
      updatedDestinations() {
        return this.destinations;
      }
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
      }
    }
  };
</script>
