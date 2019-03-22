<template>
    <v-card>
        <v-form v-model="valid">
            <v-layout wrap class="side-filter">
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model=search_params.fName
                        label="First Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model="search_params.lName"
                        label="Last Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model.number="search_params.minAge"
                        label="Min Age"
                        :rules="ageRules"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model.number="search_params.maxAge"
                        label="Max Age"
                        :rules="ageRules"
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
                        v-model="search_params.travellerTypes"
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
    data() {
        return {  
            ageRules: [
                v => (parseInt(v) >= 0 || v.length == 0) || 'Age must be positive and number'
            ],
            search_params: {
                fName: '',
                lName: '',
                minAge: '',
                maxAge: '',
                gender: '',
                nationality: '',
                travellerTypes: '',
                orderBy: ''
            }
        }
    },
    methods: {
        searchUsers: function() {
            getAllUsers(this.search_params).then(function(result) {
                store.state.users.users = result;
            })
        },
        resetSearch: function() {
            this.search_params.fName = '';
            this.search_params.lName = '';
            this.search_params.minAge = '';
            this.search_params.maxAge = '';
            this.search_params.gender = '';
            this.search_params.nationality = '';
            this.search_params.travellerTypes = '';
            this.search_params.orderBy = '';
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