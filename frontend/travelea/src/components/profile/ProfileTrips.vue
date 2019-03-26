/* eslint-disable */

<template>
  <div class="profile-outer-container">
    <div class="profile-inner-container">
      <v-layout row>
        <aside>
          <ProfileNav
            :fname.sync="traveller.firstName"
            :mname.sync="traveller.middleName"
            :lname.sync="traveller.lastName"
            :dob.sync="dateOfBirth"
            :gender.sync="traveller.gender"
            :types.sync="traveller.travellerTypes"
            :nationalities.sync="nationalities"
            :passports.sync="passports"
          />
        </aside>

        <main class="profile-main">
           
          <v-card>
            <v-btn
              class="upload-toggle-button"
              fab
              small
              dark
              color="indigo"
              @click="$router.go(-1)"
            >
         
              <v-icon dark>keyboard_arrow_left</v-icon>
            </v-btn>
              
            <trips></trips>
          </v-card>
        </main>
      </v-layout>
    </div>
  </div>
</template>

  <!-- <v-flex xs9 ml-5>
                <v-card color="blue">
                    <v-card-text>
                            
                    </v-card-text>                
                </v-card>
            </v-flex> -->

<style>
.trips-tile {
  margin-left: 20px;
}

.destinations-tile {
  margin-top: 20px;
  height: 290px;
}

.photos-tile {
  height: 300px;
}

aside {
  width: 25%;
  margin-right: 20px;
}

main {
  margin-left: 20px;
}

.profile-inner-container {
  width: 100%;
  margin: 20px;
  /* align-self: center; */
}

.profile-outer-container {
  text-align: center;
  display: flex;
  justify-content: center;
}

.profile-main {
  width: 100%;
  margin: 0px;
  text-align: left;
}
</style>


<script>
// import {RepositoryFactory} from "../../repository/RepositoryFactory"
import ProfileNav from "./profileNav";
import Trips from "../trips/Trips";
import PersonalPhotos from "./PersonalPhotos2";
import dateTime from "../common/dateTime/dateTime.js";
import ProfileRepository from "../../repository/ProfileRepository";

import { store } from "../../store/index";
// let profileRepository = RepositoryFactory.get("profile")

export default {
  name: "Profile",
  store,

  data() {
    return {
      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: []
    };
  },

  components: {
    ProfileNav,
    Trips,
    PersonalPhotos
  },

  mounted() {
    this.getTraveller();
  },

  methods: {
    getTraveller() {
      let user = store.getters.getUser;
      this.traveller = user.profile;
      this.setTravellerToFields();
    },

    setTravellerToFields() {
      [
        this.nationalities,
        this.passports
      ] = travellerFormHelper.convertFromNationalitiesRes(
        this.traveller.nationalities
      );
      this.traveller.travellerTypes = travellerFormHelper.convertFromTravellerTypesRes(
        this.traveller.travellerTypes
      );
      this.dateOfBirth = dateTime.convertTimestampToString(
        this.traveller.dateOfBirth
      );
    }
  }
};
</script>
