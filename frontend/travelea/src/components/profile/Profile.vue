/* eslint-disable */

<template>
    <v-container ma-0>
        <v-layout row>
            <v-flex xs3 mr-5>
                <profileNav>
                </profileNav>
            </v-flex>
            <v-flex xs9 ml-5>
                <v-card color="blue">
                    <v-card-text>
                            
                    </v-card-text>                
                </v-card>
            </v-flex>
        </v-layout>
        <trips></trips>
    </v-container>
</template>

<style>
</style>


<script>

// import {RepositoryFactory} from "../../repository/RepositoryFactory"
import profileNav from "./profileNav"
import Trips from  "../trips/Trips"
// let profileRepository = RepositoryFactory.get("profile")


export default {
    data () {
        return {   
        }
    },
    components: {
        profileNav,
        Trips
    },
    created: function() {  
        this.var = 5

    }
}
</script>
