/* eslint-disable */

<template>
    <v-container pa-0  >
        <v-layout column align-center class="profile-nav">
            <v-layout pb-3>
                <v-avatar size="150px">
                    <img src="https://specials-images.forbesimg.com/imageserve/5b153a1031358e612fbb0afb/416x416.jpg?background=000000&cropX1=642&cropX2=2255&cropY1=29&cropY2=1641">
                </v-avatar>
            </v-layout>
            <v-flex xs10>{{ profile.profile.firstName}} {{ profile.profile.firstName}}      </v-flex> 
            <v-flex xs10>DOB: {{ profile.profile.dateOfBirth }}       </v-flex>         
        </v-layout>
    </v-container>
</template>

<style>
.profile-nav {
    border: 1px solid black;
}


</style>


<script>

import {RepositoryFactory} from "../../repository/RepositoryFactory"
let profileRepository = RepositoryFactory.get("profile")

export default {
    data () {
        return {   
            profile:{}  
    }
    },
    created: function() {  
        this.profile = profileRepository.getProfile()
        console.log(this.profile);    
    }
}
</script>
