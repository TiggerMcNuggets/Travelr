/* eslint-disable */

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="width-for-container">
        <v-form ref="form" lazy-validation>
            <v-container grid-list-xl>
                <h4>Create a new trip</h4>
                <v-layout justify-space-around="true">
                    <v-flex xs12 md12 class="row-input-margin">
                        <v-text-field v-model="trip.title" :counter="20" label="Trip Name" required></v-text-field>
                    </v-flex>
                </v-layout>
                <ul>
                    <li v-for="(destination, index) in trip.destinations" :v-bind="index" :key="index">
                            <v-card class="destination-form-padding">
                                    <v-layout>
                                        <v-flex xs12 md4>
                                            <v-combobox
                                                    :rules="noSameDestinationNameConsecutiveRule"
                                                    :items="destinations.map(dest => dest.name)"
                                                    v-model="destination.title"
                                                    label="Select an existing destination"
                                            ></v-combobox>
                                            <v-btn flat small color="error" v-on:click="deleteDestination(index)">Remove</v-btn>
                                        </v-flex>

                                        <v-flex xs12 md4>
                                            <v-card class="times-padding">
                                                <!-- Arrival date -->
                                                <v-menu
                                                        v-model="destination.arrivalDateMenu"
                                                        :close-on-content-click="false"
                                                        :nudge-right="40"
                                                        lazy
                                                        transition="scale-transition"
                                                        offset-y
                                                        full-width
                                                        min-width="290px"
                                                >
                                                    <template v-slot:activator="{ on }">
                                                        <v-text-field
                                                                v-model="destination.arrivalDate"
                                                                label="Arrival date"
                                                                prepend-icon="event"
                                                                readonly
                                                                v-on="on"
                                                        ></v-text-field>
                                                    </template>
                                                    <v-date-picker v-model="destination.arrivalDate" @input="destination.arrivalDateMenu = false"></v-date-picker>
                                                </v-menu>
                                            </v-card>
                                        </v-flex>
                                        <v-flex xs12 md4>
                                            <v-card class="times-padding">
                                                <!-- Departure date -->
                                                <v-menu
                                                        v-model="destination.departureDateMenu"
                                                        :close-on-content-click="false"
                                                        :nudge-right="40"
                                                        lazy
                                                        transition="scale-transition"
                                                        offset-y
                                                        full-width
                                                        min-width="290px"
                                                >
                                                    <template v-slot:activator="{ on }">
                                                        <v-text-field
                                                                v-model="destination.departureDate"
                                                                label="Departure date"
                                                                prepend-icon="event"
                                                                readonly
                                                                v-on="on"
                                                        ></v-text-field>
                                                    </template>
                                                    <v-date-picker v-model="destination.departureDate" @input="destination.departureDateMenu = false"></v-date-picker>
                                                </v-menu>
                                            </v-card>
                                    </v-flex>
                                </v-layout>
                            </v-card>
                        </li>
                </ul>
                <div>
                    <v-btn color="red" v-on:click="resetValues">CANCEL</v-btn>
                    <v-btn v-on:click="createTrip">CREATE TRIP</v-btn>
                    <v-btn v-on:click="addDestinationToTrip">ADD DESTINATION</v-btn>
                </div>
            </v-container>
        </v-form>
    </div>
</template>

<style>
    @import url("https://fonts.googleapis.com/css?family=Karla:400,700");

    .width-for-container {
        width: 60%;
    }
    .destination-form-padding {
        padding: 2em;
    }
    .times-padding {
        padding: 1em;
    }
</style>


<script>
    import { store } from "../../store/index";
    import { createTrip } from '../../repository/TripRepository';

    export default {
        store,
        components: {
        },
        props: {},
        data() {
            return {
                trip: {
                    title: "",
                    destinations: []
                }
            };
        },
        computed: {
            // rules
            noSameDestinationNameConsecutiveRule() {
                let noConsecutiveSame = true;
                const destinations = this.trip.destinations;
                for (let i=0; i < destinations.length; i++) {
                    if ((i + 1) < destinations.length) {
                        if (destinations[i].title === destinations[i + 1].title) {
                            noConsecutiveSame = false;
                        }
                    }
                    if ((i - 1) >= 0) {
                        if (destinations[i].title === destinations[i - 1].title) {
                            noConsecutiveSame = false;
                        }
                    }
                }
                return [(noConsecutiveSame) || "Cannot have same destination consecutive"];
            },
            //
            destinations() {
                return store.state.destinations.destinations;
            },
        },
        methods: {
            resetValues: function() {
                this.$refs.form.reset();
            },
            deleteDestination: function(index) {
                let newDestinations = this.trip.destinations;
                newDestinations.splice(index, 1);
                this.trip.destinations = newDestinations;
            },
            addDestinationToTrip: function() {
                console.log(this.trip.destinations);
                const template = {
                        title: null,
                        arrivalDate: null,
                        departureTime: null,
                        departureDate: null,
                        arrivalDateMenu: false,
                        departureDateMenu: false,
                };
                let newDestinations = this.trip.destinations;
                newDestinations.push(template);
                this.trip.destinations = newDestinations;
            },
            createTrip: async function() {
                if (this.$refs.form.validate()) {
                    // FIXME: traveller id needs to be retrieved from STORE when the store will have it
                    // FIXME: for now traveller id is hardcoded, which means that for testing we need to create a traveller through postman first
                    let trip = {travellerID: 1, name: this.trip.title, destinations: [] };
                    this.trip.destinations.forEach((destination, index) => {
                        const destById = this.destinations.find(dest => destination.title === dest.name);
                        trip.destinations.push({destId: destById.id, ordinal: index, arrivalDate: destination.arrivalDate, departureDate: destination.departureDate});
                    });
                    console.log(trip);
                    // TODO: wait for auth frontend
                    const response = await createTrip(trip);
                    console.log(response);
                    // if (response.status) {
                    //
                    // }
                }
            }

        }
    };
</script>