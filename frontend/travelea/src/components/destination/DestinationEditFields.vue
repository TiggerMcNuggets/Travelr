<template>
    <v-layout white>
        <v-form ref="form" lazy-validation>
            <v-container grid-list-xl>
                <v-layout justify-space-around>
                    <v-flex xs12 md6 class="row-input-margin">
                        <v-text-field
                                v-model="destination.name"
                                :counter="60"
                                label="Destination Name"
                                required
                                :rules="nameRules"
                        ></v-text-field>
                    </v-flex>

                    <v-flex xs12 md6>
                        <v-text-field
                                v-model="destination.type"
                                :counter="60"
                                :rules="nameRules"
                                label="Destination Type"
                                required
                        ></v-text-field>
                    </v-flex>
                </v-layout>

                <v-layout>
                    <v-flex xs12 md6>
                        <v-text-field
                                v-model="destination.district"
                                :counter="60"
                                :rules="nameRules"
                                label="Destination District"
                                required
                        ></v-text-field>
                    </v-flex>

                    <v-flex xs12 md6>
                        <v-text-field
                                v-model="destination.country"
                                :counter="60"
                                label="Country"
                                required
                                :rules="nameRules"
                        ></v-text-field>
                    </v-flex>
                </v-layout>

                <v-layout>
                    <v-flex xs12 md6>
                        <v-text-field
                                v-model.number="destination.latitude"
                                type="number"
                                :rules="numberRules"
                                label="Latitude"
                                required
                        ></v-text-field>
                    </v-flex>

                    <v-flex xs12 md6>
                        <v-text-field
                                v-model.number="destination.longitude"
                                type="number"
                                :rules="numberRules"
                                label="Longitude"
                                required
                        ></v-text-field>
                    </v-flex>
                </v-layout>
                <div>
                    <v-btn v-on:click="commitEdits(destination)">UPDATE</v-btn>
                </div>
                <v-alert
                        :value="isError"
                        type="error"
                >
                    This destination is already available to you
                </v-alert>
            </v-container>
        </v-form>
    </v-layout>
</template>

<style>
    .outer-container {
        text-align: center;
    }

    .buttons-div {
        margin-top: 2em;
    }

    .container {
        align-self: center;
        display: inline-block;
        text-align: left;
    }
</style>


<script>
    import { rules } from "../form_rules";

    export default {
        mixins: [],
        components: {},
        props: {
            updateDestinationCallback: Function,
            prefillData: Object
        },
        data() {
            return {
                destination: {},
                isError: false,
                ...rules
            };
        },
        watch: {
            /*
             * Watches the prefillData object from destination edit which contains data about the destination being edited.
             */
            prefillData: function(newPrefillData) {
                this.destination = newPrefillData;
            }
        },
        methods: {
            /**
             * Resets the form values to blank.
             */
            resetValues: function() {
                this.$refs.form.reset();
            },

            /**
             * Redirects the user back to the previous page.
             */
            routeBackToPrevPage: function() {
                this.$router.go(-1);
            },

            /*
             * When the edit destination form is submitted by the user and is valid, calls back to the
             * parent with the destination information as the argument.
             */
            commitEdits(destination) {
                if (this.$refs.form.validate()) {
                    this.updateDestinationCallback(destination);
                }
            }
        },

        created() {
            // If the destination create window has been opened on the Map and already has latitude and longitude.
            if (this.prefillData) {
                this.destination = this.prefillData;
            }
        }
    };
</script>
