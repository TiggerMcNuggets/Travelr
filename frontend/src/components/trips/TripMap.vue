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
          :position="destination"
          :draggable="false"
          :icon="blueMarker"
        />
        <GmapPolyline
          v-for="path in paths"
          :key="path.id"
          :options="{path: path, strokeColor: path[0].strokeColor}"
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
        },
        infoWindow: {
          position: { lat: 0, lng: 0 },
          open: false
        },
        blueMarker: blueMarker
      };
    },

    props: {
      destinations: Array,
      initialDestination: Object
    },

    computed: {

      /**
      * Returns a list of paths to be rendered as polylines obtained from the pathList function.
      */
      paths() {
        return this.pathList();
      },

      /**
      * Returns a concatenated list of destinations
      */
      listOfDestinations() {
        return this.pathList().reduce((accumulator, path) => {
          return accumulator.concat([...path])
        }, []);
      },

      
    },

    methods: {

      // /**
      // * Creates an array of arrays containing latitude and longitude to represent different polylines.
      // * This is used to differentiate between different levels of nested trips.
      // */
      // pathList() {
      //   let tempPaths = [];
      //   let tempPath = [];
      //   let currentDepth;
      //   for (let i = 0; i < this.destinations.length; i++) {
      //     let RGB = ["00", "00", "00"]
      //     currentDepth = this.destinations[i].depth;
      //     if (i + 1 == this.destinations.length) {
      //       tempPath.push(this.destinations[i])
      //       RGB[((this.destinations[i - 1].depth + 2) % 3)] = "FF";
      //       tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //       tempPaths.push(tempPath);
      //       break
      //     } else if (i == 0) {
      //       tempPath.push(this.destinations[i]);
      //     } else if ((currentDepth < this.destinations[i + 1].depth) || (currentDepth < this.destinations[i - 1].depth)) {
      //       tempPath.push(this.destinations[i]);
      //
      //       if (currentDepth < this.destinations[i - 1].depth) {
      //         RGB[((this.destinations[i - 1].depth + 2) % 3)] = "FF";
      //       } else {
      //         RGB[((this.destinations[i].depth + 2) % 3)] = "FF";
      //       }
      //       tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //       tempPaths.push(tempPath);
      //       tempPath = [];
      //       tempPath.push(this.destinations[i]);
      //     } else {
      //       tempPath.push(this.destinations[i]);
      //     }
      //   }
      //   for (let i = 0; i < tempPaths.length; i++) {
      //     tempPaths[i] = tempPaths[i].map((path) => {
      //       return {
      //         ...tempPaths[i],
      //         lat: path.destination.latitude,
      //         lng: path.destination.longitude,
      //         }
      //     });
      //   }
      //
      //   if (this.destinations[0] != undefined) {
      //     this.gMapOptions.center = {lat: this.destinations[0].destination.latitude, lng: this.destinations[0].destination.longitude};
      //   }
      //   return tempPaths;
      // },

        /**
         * Creates an array of arrays containing latitude and longitude to represent different polylines.
         * This is used to differentiate between different levels of nested trips.
         */
        pathList() {
            let tempPaths = [];
            let tempPath = [];
            let currentDepth;
            for (let i = 0; i < this.destinations.length; i++) {
                if (this.destinations[i].type === "destination") {
                    let RGB = ["00", "00", "00"]
                    currentDepth = this.destinations[i];
                    if (i + 1 == this.destinations.length) {
                        tempPath.push(this.destinations[i])
                        RGB[((this.destinations[i - 1] + 2) % 3)] = "FF";
                        tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
                        tempPaths.push(tempPath);
                        break
                    } else if (i == 0) {
                        tempPath.push(this.destinations[i]);
                    } else if ((currentDepth < this.destinations[i + 1]) || (currentDepth < this.destinations[i - 1])) {
                        tempPath.push(this.destinations[i]);

                        if (currentDepth < this.destinations[i - 1]) {
                            RGB[((this.destinations[i - 1] + 2) % 3)] = "FF";
                        } else {
                            RGB[((this.destinations[i] + 2) % 3)] = "FF";
                        }
                        tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
                        tempPaths.push(tempPath);
                        tempPath = [];
                        tempPath.push(this.destinations[i]);
                    } else {
                        tempPath.push(this.destinations[i]);
                    }
                }

            }
            for (let i = 0; i < tempPaths.length; i++) {
                tempPaths[i] = tempPaths[i].map((path) => {
                    return {
                        ...tempPaths[i],
                        lat: path.destination.latitude,
                        lng: path.destination.longitude,
                    }
                });
            }

            if (this.destinations[0] != undefined) {
                this.gMapOptions.center = {lat: this.destinations[0].destination.latitude, lng: this.destinations[0].destination.longitude};
            }
            return tempPaths;
        },
    },
  };
</script>
