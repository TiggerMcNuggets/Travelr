/* eslint-disable */

<template>
    <div class="destination-map">
        <Map></Map>
        <MapMenu
                :updateDestinationsMarkers="this.updateDestinationsMarkers"
        />
    </div>
</template>

<style>
    @import url("https://fonts.googleapis.com/css?family=Karla:400,700");

    ul {
        padding-left: 0px;
    }

    h2 {
        align-self: flex-end;
    }

    hr {
        margin-bottom: 25px;
    }
    ul {
        list-style: none;
    }

    .destination-map {
        display: flex;
        flex-direction: row;
        align-content: center;
        height: 800px;
    }
</style>


<script>
    import { store } from "../../store/index";
    import Map from "../map/Map";
    import MapMenu from "../map/MapMenu";

    export default {
        store,
        // local variables
        data() {
            return {
                destinationsMarkers: []
            };
        },
        props: {
        },
        computed: {
        },
        // child components
        components: {
            Map: Map,
            MapMenu,
        },
        watch: {
            'this.destinationsMarkers': function () {
                console.log(this.destinationsMarkers);
            } 
        },
        methods: {
            updateDestinationsMarkers(toDelete, newValue) {
                if (toDelete) {
                    this.destinationsMarkers.push(newValue);
                }
            }
        },
        created() {
            this.destinationsMarkers = this.destinations;
        }
    };
</script>
