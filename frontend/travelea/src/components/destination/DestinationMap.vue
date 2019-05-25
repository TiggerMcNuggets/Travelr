<template>
    <v-layout>            
      <div class="destination-overlayed">
        <v-label>
          <GmapAutocomplete
            placeholder="Search"
            @place_changed="usePlace"
            class="v-text-field destination-search-box"
            :select-first-on-enter="true"
            @keydown.native.enter.prevent
          ></GmapAutocomplete>
          <button class="destination-search-btn" @click="usePlace">
            <i aria-hidden="true" class="v-icon material-icons">search</i>
          </button>
        </v-label>
      </div>
      <v-flex class="map-flex">
      <GmapMap class="destination-main-map" :zoom="2" :center="{lat: 0, lng: 0}" ref="map" :options="{mapTypeControl: false}" @click="onMapClick($event)">   
          

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

  export default {
    data() {
      return {       
      };
    },

    props: {
      destinations: Array,
      focusDestination: Function
    },
    components: {     
    },

    methods: {
      
      /**
       * Logs the event to the window.
       * @param evt The event to log.
       */
      log: function(evt) {
        window.console.log(evt);
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
        this.place = place;

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
      updateCoordinatesAfterDrag(location, destinationMarkerIndex) {
        this.destinationMarkers[destinationMarkerIndex] = {
          latitude: parseFloat(location.latLng.lat()), longitude: parseFloat(location.latLng.lng())
        };
      },

      /*
       * Perceives a click on the map and creates a destination at the click location
       */
      onMapClick(clickEvent) {
        this.usePlace({
          geometry: {
            location: {
              lat: clickEvent.latLng.lat,
              lng: clickEvent.latLng.lng
            }
          },
        });
      }
    }
  };
</script>
