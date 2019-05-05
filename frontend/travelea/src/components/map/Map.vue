<template>
    <div class="map-container" >
        <!--<h1>Autocomplete Example (#164)</h1>-->
        <!--<label>-->
            <!--AutoComplete-->
            <!--<GmapAutocomplete-->
                    <!--placeholder="This is a placeholder text"-->
                    <!--@place_changed="setPlace">-->
            <!--</GmapAutocomplete>-->
            <!--<button @click="usePlace">Add</button>-->
        <!--</label>-->
        <!--<br/>-->
        <GmapMap class="main-map"
                 :zoom="2"
                 :center="{lat: 0, lng: 0}"
                 ref="map"
        >
            <GmapMarker v-for="(marker, index) in destinationsMarkers"
                        :key="index"
                        :position="{lat: marker.latitude, lng: marker.longitude}"
                        :clickable="true"
                        @click="openInfoWindowTemplate(destinationsMarkers[index])"
                        :icon="chooseIconForMarker(marker, index)"
            />
            <GmapInfoWindow
                    :options="{maxWidth: 300}"
                    :position="infoWindow.position"
                    :opened="infoWindow.open"
                    @closeclick="closeInfoWindow()">
                <div v-if="selectedDest !== null">
                    <div>
                        <h2>Country: {{this.selectedDest.country}}</h2>
                        <h2>District: {{this.selectedDest.district}}</h2>
                        <h2>Name: {{this.selectedDest.name}}</h2>
                        <h2>Type: {{this.selectedDest.type}}</h2>
                        <v-btn color="orange darken-2" dark v-on:click="navigateToDestination(selectedDest.id)">
                            Visit<v-icon dark right>arrow_forward</v-icon>
                        </v-btn>
                    </div>
                </div>
            </GmapInfoWindow>
        </GmapMap>
    </div>
</template>

<style>
    .map-container {
        width: 100%;
        height: 800px;

    }
    .main-map {
        width: 100%;
        height: 100%;
    }
</style>

<script>
    const pinkMarker = require('../../assets/pink-google-maps-marker.svg');
    const blueMarker = require('../../assets/blue-google-maps-marker.svg');
    export default {
        data () {
            return {
                userId: (this.$route.params.user_id) ? this.$route.params.user_id : this.$route.params.id,
                infoWindow: {
                    position: {lat: 0, lng: 0},
                    open: false,
                },
                selectedDest: null,
                // markers: [{latLng: {lat: 0, lng: 0}, }, {latLng: {lat: 30, lng: 100}}],
                place: null,
            }
        },
        props: {
            destinationsMarkers: Array
        },
        watch: {
        },
        components: {
        },
        methods: {
            openInfoWindowTemplate(item) {
                this.infoWindow.position = {lat:item.latitude, lng: item.longitude};
                this.infoWindow.open = true;
                this.selectedDest = item;
            },
            closeInfoWindow() {
                this.infoWindow.open = false;
                this.selectedDest = null;
            },
            log: function(evt) {
                window.console.log(evt);
            },
            navigateToDestination: function(destId) {
                this.$router.push(`/user/${this.userId}/destinations/${destId}`);
            },
            chooseIconForMarker(marker, index) {
              if ((index % 2) === 0) {
                  return pinkMarker;
              } else {
                  return blueMarker;
              }
            },
            // setPlace(place) {
            //     this.place = place
            // },
            // usePlace() {
            //     if (this.place) {
            //         this.markers.push({
            //             position: {
            //                 lat: this.place.geometry.location.lat(),
            //                 lng: this.place.geometry.location.lng(),
            //             }
            //         });
            //         this.place = null;
            //     }
            // }
        },
        created() {
        }
    }
</script>