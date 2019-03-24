<template>
    <v-card>
        <v-form>
            <v-layout wrap class="side-filter">
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model="search_params.firstName"
                        label="First Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model="search_params.lastName"
                        label="Last Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model.number="search_params.minAge"
                        label="Min Age"
                        type='number'
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model.number="search_params.maxAge"
                        label="Max Age"
                        type='number'
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="search_params.gender"
                        :items="['Male', 'Female']"
                        chips
                        attach
                        label="Gender"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="search_params.nationality"
                        :items="nationalities"
                        attach
                        chips
                        multiple
                        label="Nationality"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="search_params.travellerType"
                        :items="travellerTypes"
                        attach
                        chips
                        multiple
                        label="Traveller Type"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12 >
                        <v-select
                        v-model="search_params.orderBy"
                        :items="['Nationality', 'Traveller Type']"
                        attach
                        chips
                        label="Order By"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12 class="text-xs-center">
                        <v-btn color='primary' v-on:click="searchUsers">Search</v-btn>
                        <v-btn color='primary' v-on:click="resetSearch">Reset</v-btn>
                    </v-flex>
            </v-layout>
        </v-form>
    </v-card>
</template>

<style>
    .side-filter {
        padding: 20px;
    }
</style>

<script>
import { getAllUsers } from "../../repository/UserRepository";
import { store } from "../../store/index";

export default {
    store,
    data() {
        return {  
            ageRules: [
                v => (parseInt(v) >= 0 || v.length == 0) || 'Age must be positive and number'
            ],
            search_params: {
                firstName: null,
                lastName: null,
                gender: null,
                minAge: 0,
                maxAge: 150,
                nationality: null,
                travellerType: null,
                orderBy: null
            }
        }
    },
    methods: {
        async searchUsers() {
            await store.dispatch("getUsers", this.search_params);
            
            /*getAllUsers(this.search_params).then(function(result) {
                store.state.users.users = result;
            })*/
        },
        resetSearch: function() {
            this.search_params.firstName = null;
            this.search_params.lastName = null;
            this.search_params.gender = null;
            this.search_params.minAge = null;
            this.search_params.maxAge = null;
            this.search_params.nationality = null;
            this.search_params.travellerTypes = null;
            this.search_params.orderBy = null;
            this.searchUsers();
        }
    },
    // the place where you want to make the store values readable
    computed: {
        nationalities() {
            return store.state.users.nationalities;
        },
        travellerTypes() {
            return store.state.users.travellerTypes;
        }
    },
    created: async function() {
        store.commit("setNationalities");
        store.commit("setTravellers");
    }
}
</script>