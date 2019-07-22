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
          v-for="path in pathList"
          :key="path.ordinal"
          :position="{lat: path.lat, lng: path.lng}"
          :draggable="false"
          :icon="privateMarker"
        />
        <GmapPolyline
          v-for="path in pathList"
          :key = path.ordinal
          :path = path
          :options="{strokeColor: path.strokeColor}"
          :strokeWeight = 1
          >
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
          strokeColor: '#0000FF'
        },
        infoWindow: {
          position: { lat: 0, lng: 0 },
          open: false
        },
        publicMarker: blueMarker,
        privateMarker: pinkMarker,
        trip: [
            {lat: -43.53205440000001, lng: 172.63622540000006, ordinal: 1, depth: 1},
            {lat: -36.8484597, lng: 174.76333150000005, ordinal: 2, depth: 1},
            {lat: -33.8688197, lng: 151.20929550000005, ordinal: 3, depth: 2},
            {lat: 41.90278349999999, lng: 12.496365500000024, ordinal: 4, depth: 3},
            {lat: -17.6509195, lng: -149.42604210000002, ordinal: 5, depth: 3},
            {lat: -17.6509195, lng: -149.42604210000002, ordinal: 6, depth: 2},
            {lat: 55.9366784, lng: -4.7739458, ordinal: 7, depth: 1}
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
      /*
      * Creates an array of arrays containing latitude and longitude to represent different polylines.
      * This is used to differentiate between different levels of nested trips.
      */
      pathList() {
        let tempPaths = [];
        let tempPath = [];
        let currentDepth;
        for (let i = 0; i < this.trip.length; i++) {
          console.log(i);
          let RGB = ["00", "00", "00"]
          currentDepth = this.trip[i].depth;
          if (i + 1 == this.trip.length) {
            tempPath.push(this.trip[i])
            RGB[(this.trip[i - 1].depth - 1)] = "FF";
            tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
            tempPaths.push(tempPath);
            break
          } else if (i == 0) {
            tempPath.push(this.trip[i]);
          } else if ((currentDepth < this.trip[i + 1].depth) || (currentDepth < this.trip[i - 1].depth)) {
            tempPath.push(this.trip[i]);
            if (currentDepth < this.trip[i - 1].depth) {
              RGB[(this.trip[i - 1].depth - 1)] = "FF";
            } else {
              RGB[(this.trip[i].depth - 1)] = "FF";
            }
            tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
            tempPaths.push(tempPath)
            tempPath = [];
            tempPath.push(this.trip[i]);
          } else {
            tempPath.push(this.trip[i]);
          }
        }
        console.log(tempPaths);
        return tempPaths;
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
      },
    }
  };
</script>
