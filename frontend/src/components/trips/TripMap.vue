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
          v-for="(destination, i) in listOfDestinations"
          :key="i"
          :position=destination
          :draggable="false"
          :icon="blueMarker"
        />
        <GmapPolyline
          v-for="path in pathList"
          :key=path.ordinal
          :path=path
          :options="{strokeColor: path[0].strokeColor}"
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
        blueMarker: blueMarker
      };
    },

    props: {
      destinations: Array
    },

    created() {
      console.log("test" + this.destinations);
    },

    computed: {

      listOfDestinations() {
        return this.pathList.reduce((accumulator, path) => {
          return accumulator.concat([...path])
        }, []);
      },

      /*
      * Creates an array of arrays containing latitude and longitude to represent different polylines.
      * This is used to differentiate between different levels of nested trips.
      */
      pathList() {
        let tempPaths = [];
        let tempPath = [];
        let currentDepth;
        for (let i = 0; i < this.destinations.length; i++) {
          console.log(i);
          let RGB = ["00", "00", "00"]
          currentDepth = this.destinations[i].depth;
          if (i + 1 == this.destinations.length) {
            tempPath.push(this.destinations[i])
            RGB[(this.destinations[i - 1].depth) % 3] = "FF";
            tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
            tempPaths.push(tempPath);
            break
          } else if (i == 0) {
            tempPath.push(this.destinations[i]);
          } else if ((currentDepth < this.destinations[i + 1].depth) || (currentDepth < this.destinations[i - 1].depth)) {
            tempPath.push(this.destinations[i]);
            if (currentDepth < this.destinations[i - 1].depth) {
              RGB[(this.destinations[i - 1].depth % 3)] = "FF";
            } else {
              RGB[(this.destinations[i].depth % 3)] = "FF";
            }
            tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
            tempPaths.push(tempPath)
            tempPath = [];
            tempPath.push(this.destinations[i]);
          } else {
            tempPath.push(this.destinations[i]);
          }
        }
        console.log(tempPaths);
        console.log("here")
        for (let i = 0; i < tempPaths.length; i++) {
          tempPaths[i] = tempPaths[i].map((path) => {
            return {
              ...tempPaths[i],
              lat: path.destination.latitude,
              lng: path.destination.longitude,
              }
          });
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
