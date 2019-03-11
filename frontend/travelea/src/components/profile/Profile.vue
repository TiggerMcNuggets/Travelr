/* eslint-disable */

<template>
    <v-container ma-0 >
        <v-layout row >
            <v-flex xs2>
                <v-card color="green">
                    <v-card-text>
                            Nav
                    </v-card-text>                
                </v-card>
            </v-flex>
            <v-flex xs10>
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
let profileRepository = RepositoryFactory.get("profile")

export default {
    el: '#profile',
    data () {
        return {     
    }
    },
    created: function() {  
        profileRepository      
    }
}
</script>
