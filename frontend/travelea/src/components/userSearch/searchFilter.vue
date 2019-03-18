<template>
    <v-card>
            <v-layout wrap>
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
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model.number="search_params.maxAge"
                        label="Max Age"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-combobox
                        v-model="search_params.gender"
                        :items="['Male', 'Female']"
                        attach
                        chips
                        label="Gender"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-combobox
                        v-model="search_params.nationality"
                        :items="['Traveller1', 'Traveller2', 'Traveller3']"
                        chips
                        label="Nationality"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="search_params.travellerTypes"
                        :items="['Traveller1', 'Traveller2', 'Traveller3']"
                        attach
                        chips
                        multiple
                        label="Traveller Type"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12 >
                        <v-combobox
                        v-model="search_params.orderBy"
                        :items="['Nationality', 'Traveller Type']"
                        attach
                        chips
                        label="Order By"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm12 md12 class="text-xs-center">
                        <v-btn color='primary' v-on:click="searchUsers">Search</v-btn>
                    </v-flex>
            </v-layout>
    </v-card>
</template>

<script>
import { getAllUsers } from "../../repository/UserRepository";
import { store } from "../../store/index";

export default {
    data() {
        return {  
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
        }
    }
}
</script>