<template>
  <v-flex lg5 pa-2>
    <v-layout>
      <v-flex class="map-flex">
        <GmapMap
          class="destination-main-map"
          :center="gMapOptions.center"
          ref="map"
          :options="gMapOptions"
        >
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
  </v-flex>
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
      };
    },

    props: {
      nodes: Array
    },

    computed: {

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
          let point2 = this.allMarkers[i + 1].position;
          path.push(point1, point2);
          singlePath.path = path;

          if (this.allMarkers[i].type === "trip" && this.allMarkers[i + 1].type === "trip") {
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
      isAdjacentToTrip(positionOfDestination) {
        let previousIndex = positionOfDestination - 1;
        let nextIndex = positionOfDestination + 1;
        if (previousIndex >= 0) {
          let previousNode = this.nodes[previousIndex];
          if (previousNode.type === "trip") {
            return true;
          }
        }
        if (nextIndex < (this.nodes).length) {
          let nextNode = this.nodes[nextIndex];
          if (nextNode.type === "trip") {
            return true;
          }
        }
      },
    }
  };
</script>
