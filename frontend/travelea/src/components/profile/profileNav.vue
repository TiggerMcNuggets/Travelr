/* eslint-disable */

<template>
    <v-container pa-0  >
        <v-layout column align-center class="profile-nav">
            <v-layout pb-3>
                <v-avatar size="150px">
                    <img src="https://specials-images.forbesimg.com/imageserve/5b153a1031358e612fbb0afb/416x416.jpg?background=000000&cropX1=642&cropX2=2255&cropY1=29&cropY2=1641">
                </v-avatar>
            </v-layout>
            <v-flex xs10>{{ profile.firstName}} {{ profile.firstName}}      </v-flex> 
            <v-flex xs10>DOB: {{ profile.dateOfBirth }}       </v-flex>         
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
let profileRepository = RepositoryFactory.get("profile");
import { store } from "../../store/index";


export default {
    store,
    data () {
        return {   
            profile:{}  
    }
    },
    created: async function() {  
//        let user_id = store.state.user.getId;
//        console.log(user_id);
//        this.profile = await profileRepository.getProfile(user_id);
        this.profile = await profileRepository.getProfile();
        console.log(this.profile);    
    }
}
</script>
