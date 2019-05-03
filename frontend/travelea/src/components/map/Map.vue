<template>
    <div class="map-container" >
        <GmapMap class="main-map" :zoom="2" :center="{lat: 0, lng: 0}"
                 ref="map">
            <GmapMarker v-for="(marker, index) in markers"
                        :key="index"
                        :position="marker.latLng"
                        :clickable="true"
                        @click="openInfoWindowTemplate(markers[index])"
            />
            <GmapInfoWindow
                    :options="{maxWidth: 300}"
                    :position="infoWindow.position"
                    :opened="infoWindow.open"
                    @closeclick="infoWindow.open=false">
                <div>
                    <h1>Sometjing</h1>
                    <v-btn>Submit</v-btn>
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

    export default {
        data () {
            return {
                infoWindow: {
                    position: {lat: 0, lng: 0},
                    open: false,
                    template: 'Ciaooojdkajsfjkdsbfkjsdbf'
                },
                markers: [{latLng: {lat: 0, lng: 0}, }, {latLng: {lat: 30, lng: 100}}],
                place: null,
            }
        },
        watch: {
        },
        components: {
        },
        methods: {
            openInfoWindowTemplate(item) {
                this.infoWindow.position = item.latLng;
                this.infoWindow.open = true;
            },
            log: function(evt) {
                window.console.log(evt);
            }
        }
    }
</script>