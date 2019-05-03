
<template>
    <div class="side-menu-height map-side-menu-container">
        <div class="blue-background side-menu-button-size">
            <v-icon color="white">drag_handle</v-icon>
        </div>
        <div class="side-menu-height side-menu-width">
            <div
                    class="destination-list-element"
                    v-for="(item) in this.selectedDestinations"
                    :value="item.value"
                    :key="item.value"
            >
                <v-checkbox
                        v-model="item.selected"
                        :label="item.name" :value="item.name"></v-checkbox>
                <v-divider class="photo-header-divider"></v-divider>

            </div>
        </div>
    </div>
</template>

<style>
    @import url("https://fonts.googleapis.com/css?family=Karla:400,700");

    h2 {
        align-self: flex-end;
    }

    hr {
        margin-bottom: 25px;
    }
    .dest-name div {
        text-align: start;
    }
    .dest-sub-info p {
        margin-bottom: 0px;
        color: grey;
    }

    .dest-name button {
        margin-right: 20px;
    }

    .horizontal-details li {
        display: inline-block;
        padding-right: 20px;
        color: white;
    }


    .top-destination-content span {
        padding: 10px 20px;
    }

    .top-destination-content a {
        font-family: "Karla", sans-serif;
        font-size: 15px;
        margin-left: 10px;
    }

    ul {
        list-style: none;
    }


    .blue-background {
        background-color: #3f51b5;
        width: 15px;
    }

    .side-menu-button-size {
        height: 100%;
        width: 30px;
    }
    .side-menu-height {
        height: 100%;
        width: 100%;
    }
    .map-side-menu-container {
        width: 15%;
        display: flex;
        align-items: start;
        flex-direction: row;
    }

</style>


<script>
    import {RepositoryFactory} from "../../repository/RepositoryFactory";
    let destinationRepository = RepositoryFactory.get("destination");
    export default {
        data() {
            return {
                destinations: [],
                selectedDestinations: []
            };
        },
        props: {
            updateDestinationsMarkers: Function
        },
        // child components
        components: {
        },
        methods: {
        },
        created: function() {
            // destinations = await destinationRepository.getDestinationList()

        }
    };
</script>
