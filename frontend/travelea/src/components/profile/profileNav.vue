/* eslint-disable */

<template>
  <!-- <v-container pa-0  >
        <v-layout column align-center class="profile-nav">
            <v-layout pb-3>
                <v-avatar size="150px">
                    <img src="https://specials-images.forbesimg.com/imageserve/5b153a1031358e612fbb0afb/416x416.jpg?background=000000&cropX1=642&cropX2=2255&cropY1=29&cropY2=1641">
                </v-avatar>
            </v-layout>
            <v-flex xs10>{{ profile.firstName}} {{ profile.firstName}}      </v-flex> 
            <v-flex xs10>DOB: {{ profile.dateOfBirth }}       </v-flex>         
        </v-layout>
  </v-container>-->
  <v-card class="profile-sidebar">
   
    <div>
      
      <v-avatar size="100px" class="profile-photo">
        <img
          src="https://specials-images.forbesimg.com/imageserve/5b153a1031358e612fbb0afb/416x416.jpg?background=000000&cropX1=642&cropX2=2255&cropY1=29&cropY2=1641"
        >
      </v-avatar>
      <router-link to="/profile/edit">
       <v-btn
          class="profile-edit-button"
          fab
          small
          dark
          color="indigo"
        >
          <v-icon dark>edit</v-icon>
        </v-btn>
         </router-link>
    </div>

    <div>
      <div class='profile-heading-section'>
      <h3 class="headline font-weight-light traveller-name">{{fname}}</h3>
      <p class="caption font-weight-light">{{email}}</p>
      </div>
      <v-divider></v-divider>

        <div class="field-section duel-section">
      <div class="field-section">
        <p class="body-1 font-weight-normal">{{dob}}</p>
        <p class="caption font-weight-light field-title">DOB</p>
      </div>
       <div class="field-section">
        <p class="body-1 font-weight-normal">{{gender}}</p>
        <p class="caption font-weight-light field-title">GENDER</p>
      </div>
    </div>
       <v-divider></v-divider>

      <div class="field-section">
        <p class="body-1 font-weight-normal">{{nationalities}}</p>
        <p class="caption font-weight-light field-title">NATIONALITIES</p>
      </div>
       <v-divider></v-divider>

  

      
      <div class="field-section">
      <p class="caption font-weight-light field-title">TRAVELLER TYPES</p>
         <li v-for="type in types" :value="type.value" :key="type.value">
            <v-chip>{{type.name}}</v-chip>
        </li>
    </div>

    </div>
  </v-card>
</template>

<style>
.profile-edit-button {
  position: absolute;
  right: 25%;
  top: 100px;
  
}

.profile-edit-button:active {
  position: absolute;
  right: 25%;
  top: 100px;
  
}

li {
    list-style: none;
}

.duel-section {
    display: flex;
    justify-content: space-between;
}

.traveller-name {
  font-size: 36px;
  line-height: 0px;
  padding-bottom: 5px;
}

.profile-heading-section {
    margin: 30px 0px;;
}

.field-section {
  text-align: left;
}

.field-title {
  line-height: 0px;
  color: grey;
}

.profile-sidebar {
  /* display: flex;
    justify-content: center; */
   

  text-align: center;
  padding: 20px;
  /* background-color: #1a1a1a69; */
  /* border-right: 1px solid grey; */
  height: 100%;
  /* color: white; */
}

.profile-photo {
  margin: 17px 0px;
  clear: both;
}
.profile-nav {
  border: 1px solid black;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let profileRepository = RepositoryFactory.get("profile");
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";


import { store } from "../../store/index";

export default {
  store,

  props: ["fname", "mname", "lname", "dob", "gender", "types", "nationalities", "passports", "email"],

  data() {
    return {
      profile: {}
    };
  },
  created: function() {
    this.profile = profileRepository.getProfile();
    console.log(this.profile);
  }
};

</script>
