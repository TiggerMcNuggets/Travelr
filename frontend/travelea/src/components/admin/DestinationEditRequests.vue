<template>
    <v-container class="outer-container" height="100%" style="margin-top: 10px; padding-left: 22.5%;">
        <v-card style="width: 70%;">
            <div class="section">
                <div class="dest-name">
                    <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
                        <v-icon dark>keyboard_arrow_left</v-icon>
                    </v-btn>

                    <h2 class="headline">Destination Edit Requests</h2>

                </div>
            </div>

            <v-divider class="photo-header-divider"></v-divider>
            <li v-for="request in editRequests" :key="request.id" style="padding: 20px">
                <v-card>
                    <v-layout row wrap s>
                        <v-flex xs12 sm12 md12 style="text-align: center;">
                            <h3>{{ request.destination.name }}</h3>
                            <v-divider/>
                        </v-flex>
                        <v-flex xs12 sm12 md2 style="text-align: center; padding-left: 20px">
                            <v-btn v-on:click="$router.push('/user/'+request.user.id)">
                                View User
                            </v-btn>
                        </v-flex>
                        <v-flex xs12 sm12 md2 style="text-align: center; padding-left: 20px">
                            <v-btn v-on:click="$router.push('/user/' + userid + '/destinations/' + request.destination.id)">
                                View Destination
                            </v-btn>
                        </v-flex>
                        <v-flex xs12 sm12 md3 offset-lg1>
                            <v-select
                                    :items="getTravellerTypes(request.destination.travellerTypes)"
                                    label="Current Traveller Types"
                                    attach
                                    solo
                            ></v-select>
                        </v-flex>
                        <v-flex xs12 sm12 md3>
                            <v-select
                                    :items="getTravellerTypes(request.travellerTypes)"
                                    label="Sugguested Traveller Types"
                                    attach
                                    solo
                            ></v-select>
                        </v-flex>
                        <v-flex xs12 sm12 md3 offset-lg3 style="text-align: center;">
                            <v-btn color="warning" v-on:click="denyRequest(request.id)">
                                Deny
                            </v-btn>
                        </v-flex>
                        <v-flex xs12 sm12 md3 style="text-align: center;">
                        <v-btn color="info" v-on:click="acceptRequest(request.id)">
                            Accept
                        </v-btn>
                    </v-flex>
                    </v-layout>
                </v-card>
            </li>

        </v-card>
    </v-container>
</template>

<script>
    import { RepositoryFactory } from "../../repository/RepositoryFactory";
    import {store} from "../../store/index";
    let destinationRepository = RepositoryFactory.get("destination");
    export default {
        store,
        data() {
            return {
                userid: null,
                editRequests: []
            };
        },
        created: function(){
            this.userid = store.getters.getUser.id;
            // Gets the edit requests
            this.init();
        },

        methods: {
            init() {
                // Gets the edit requests
                destinationRepository
                    .getEditRequests()
                    .then(response => {
                        this.editRequests = response.data.requests;
                    })
                    .catch(err => {
                        console.log(err);
                    });
            },
            denyRequest(requestId) {
                destinationRepository
                    .denyEditRequest(requestId)
                    .then(() => {
                        this.init();

                    })
                    .catch(err => {
                        console.log(err);
                    });
            },
            acceptRequest(requestId) {
                destinationRepository
                    .acceptEditRequest(requestId)
                    .then(() => {
                        this.init();
                    })
                    .catch(err => {
                        console.log(err);
                    });
            },
            getTravellerTypes(travellerTypes) {
                if (travellerTypes !== null) {
                    let travellerTypeList = [];
                    for (let i = 0; i < travellerTypes.length; i++) {
                        travellerTypeList.push(travellerTypes[i].name);
                    }
                    return travellerTypeList;
                } else {
                    return [];
                }
            }
        }
    };
</script>
