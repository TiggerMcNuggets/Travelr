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
          v-for="(destination, i) in allMarkers"
          :key="i"
          :position="destination.position"
          :draggable="false"
          :icon="destination.icon"
        />
        <GmapPolyline
          v-for="path in allPaths"
          :key="path.id"
          :options="{path: path.path, strokeColor: path.color}"
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
  import {GoogleMapLightStyle} from "../../assets/google-map-light-style"

  // resources
  import redMarker from "../../assets/red_map_icon.svg";
  import blueMarker from "../../assets/blue_map_icon.svg";

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
          position: {lat: 0, lng: 0},
          open: false
        },
        blueMarker: blueMarker,
        redMarker: redMarker,
        blueLineColor: '#1976d2',
        redLineColor: '#ff5555ff',
        testPath: [{
          lat: 77,
          lng: 77
                  },
          {
            lat: 66,
            lng: 66
          },
                  ]
      };
    },

    props: {
      nodes: Array
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
      /**
       * creates all the location markers for the map
       */
      allMarkers() {
        let mapMarkers = [];
        for (let i = 0; i < this.nodes.length; i++) {
          let singleMarker = new Object();
          if (this.nodes[i].type === "destination") {
              let markerPosition = {
              lat: this.nodes[i].destination.latitude,
              lng: this.nodes[i].destination.longitude
              };
              singleMarker.destination = this.nodes[i];
              singleMarker.position = markerPosition;
              if (this.isAdjacentToTrip(i)) {
                singleMarker.icon = this.redMarker;
                singleMarker.type = "trip"
              } else {
                singleMarker.icon = this.blueMarker;
                singleMarker.type = "destination"
              }
              mapMarkers.push(singleMarker)
          }
        }
        return mapMarkers;
      },
      /**
       * creates all the paths between locations for the map
       */
      allPaths() {
        let paths = [];
        //paths are created between current marker and next marker, why for loop stops at second to last marker
        for (let i = 0; i < (this.allMarkers.length - 1); i++) {
            let singlePath = new Object();
            let path = [];
            let point1 = this.allMarkers[i].position;
            let point2 = this.allMarkers[i+1].position;
            path.push(point1, point2);
            singlePath.path = path;

            if(this.allMarkers[i].type === "trip" && this.allMarkers[i+1].type === "trip"){
              singlePath.color = this.redLineColor;
            } else {
              singlePath.color = this.blueLineColor;
            }
            paths.push(singlePath);
          }
          return paths;

        }

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

      // /**
      //  * Creates an array of arrays containing latitude and longitude to represent different polylines.
      //  * This is used to differentiate between different levels of nested trips.
      //  */
      // pathList() {
      //   let tempPaths = [];
      //   let tempPath = [];
      //   let currentDepth;
      //   for (let i = 0; i < this.destinations.length; i++) {
      //     if (this.destinations[i].type === "destination") {
      //       let RGB = ["00", "00", "00"];
      //       currentDepth = this.destinations[i];
      //       if (i + 1 == this.destinations.length) {
      //         tempPath.push(this.destinations[i]);
      //         RGB[((this.destinations[i - 1] + 2) % 3)] = "FF";
      //         tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //         tempPaths.push(tempPath);
      //         break
      //       } else if (i == 0) {
      //         tempPath.push(this.destinations[i]);
      //       } else if ((currentDepth < this.destinations[i + 1]) || (currentDepth < this.destinations[i - 1])) {
      //         tempPath.push(this.destinations[i]);
      //
      //         if (currentDepth < this.destinations[i - 1]) {
      //           RGB[((this.destinations[i - 1] + 2) % 3)] = "FF";
      //         } else {
      //           RGB[((this.destinations[i] + 2) % 3)] = "FF";
      //         }
      //         tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //         tempPaths.push(tempPath);
      //         tempPath = [];
      //         tempPath.push(this.destinations[i]);
      //       } else {
      //         tempPath.push(this.destinations[i]);
      //       }
      //     }
      //
      //   }
      //   for (let i = 0; i < tempPaths.length; i++) {
      //     tempPaths[i] = tempPaths[i].map((path) => {
      //       return {
      //         ...tempPaths[i],
      //         lat: path.destination.latitude,
      //         lng: path.destination.longitude,
      //
      //       }
      //     });
      //   }
      //
      //   if (this.destinations[0] != undefined) {
      //     this.gMapOptions.center = {
      //       lat: this.destinations[0].destination.latitude,
      //       lng: this.destinations[0].destination.longitude
      //     };
      //   }
      //   return tempPaths;
      // },
      // /**
      //  * Creates an array of arrays containing latitude and longitude to represent different polylines.
      //  * This is used to differentiate between different levels of nested trips.
      //  */
      // pathList() {
      //   let mapPath = [];
      //   let currentDepth;
      //   let Red = "#FF0000";
      //   let Blue = "#4400FF";
      //   for (let i = 0; i < this.nodes.length; i++) {
      //     if (this.nodes[i].type === "destination") {
      //       // currentDepth = this.destinations[i];
      //       if (this.isAdjacentToTrip(i, this.nodes)) {
      //         //could use Objects here if you want to name things
      //         mapPath.push([this.nodes[i], Red]);
      //
      //         tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //         break
      //        else if (i == 0) {
      //         tempPath.push(this.destinations[i]);
      //       } else if ((currentDepth < this.destinations[i + 1]) || (currentDepth < this.destinations[i - 1])) {
      //         tempPath.push(this.destinations[i]);
      //
      //         if (currentDepth < this.destinations[i - 1]) {
      //           RGB[((this.destinations[i - 1] + 2) % 3)] = "FF";
      //         } else {
      //           RGB[((this.destinations[i] + 2) % 3)] = "FF";
      //         }
      //         tempPath.strokeColor = "#" + RGB[0] + RGB[1] + RGB[2];
      //         tempPaths.push(tempPath);
      //         tempPath = [];
      //         tempPath.push(this.destinations[i]);
      //       } else {
      //         tempPath.push(this.destinations[i]);
      //       }
      //     }
      //
      //   }
      //   for (let i = 0; i < tempPaths.length; i++) {
      //     tempPaths[i] = tempPaths[i].map((path) => {
      //       return {
      //         ...tempPaths[i],
      //         lat: path.destination.latitude,
      //         lng: path.destination.longitude,
      //
      //       }
      //     });
      //   }
      //
      //   if (this.destinations[0] != undefined) {
      //     this.gMapOptions.center = {
      //       lat: this.destinations[0].destination.latitude,
      //       lng: this.destinations[0].destination.longitude
      //     };
      //   }
      //   return tempPaths;
      // }


    // },
      isAdjacentToTrip(positionOfDestination){
        let previousIndex = positionOfDestination - 1;
        let nextIndex = positionOfDestination + 1;
        if(previousIndex > 0) {
          let previousNode = this.nodes[previousIndex];
          if(previousNode.type === "trip"){
            console.log("HERE!");
            return true;
          }
        } else if(nextIndex < this.nodes.length){
          let nextNode = this.nodes[nextIndex];
          if(nextNode.type === "trip"){
            return true;
          }
        } else{
          return false;
        }

      },
    }
  };
</script>
