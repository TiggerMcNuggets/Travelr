/* eslint-disable */

<template>
    <v-container ma-0 >
        <v-layout row >
            <v-flex xs3 mr-5>
                <profileNav>
                </profileNav>
            </v-flex>
            <v-flex xs9 ml-5>
                <v-card color="blue">
                    <v-card-text>
                            Data
                    </v-card-text>                
                </v-card>
            </v-flex>           
        </v-layout>
    </v-container>
</template>

<style>
.test {
    border: 1px solid black;
}


</style>


<script>

import {RepositoryFactory} from "../../repository/RepositoryFactory"
import profileNav from "./profileNav"
let profileRepository = RepositoryFactory.get("profile")

export default {
    data () {
        return {     
        }
    },
    components: {
        profileNav
    },
    created: function() {  
        profileRepository      
    }
}
</script>
