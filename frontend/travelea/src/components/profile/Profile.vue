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
          <v-layout row>
            <v-flex d-flex xs4 order-xs5>
              <v-layout column>
                <router-link to="/profile/photos">
                  <v-card d-flex class="photos-tile">
                    <v-img
                      src="https://cdn.vuetifyjs.com/images/cards/desert.jpg"
                      aspect-ratio="2.75"
                    ></v-img>

                    <h5>Photos</h5>
                  </v-card>
                </router-link>
                <router-link to="/profile/destinations">
                  <v-card d-flex class="destinations-tile">
                    <v-img
                      src="https://cdn.vuetifyjs.com/images/cards/desert.jpg"
                      aspect-ratio="2.75"
                    ></v-img>
                    <h5>Destinations</h5>
                  </v-card>
                </router-link>
              </v-layout>
            </v-flex>
            <v-flex d-flex x8 order-xs5>
              <v-layout column>
                <v-flex d-flex>
                  <router-link to="/profile/trips">
                    <v-card d-flex class="trips-tile">
                      <h5>Trips</h5>
                      <trips></trips>
                    </v-card>
                  </router-link>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-layout>
        </main>
      </v-layout>
    </div>
  </div>
</template>


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
}

.profile-outer-container {
  text-align: center;
  display: flex;
  justify-content: center;
}

.profile-main {
  width: 100%;
  margin: 0px;
}
</style>


<script>
import ProfileNav from "./profileNav";
import Trips from "../trips/Trips";
import PersonalPhotos from "./PersonalPhotos2";
import dateTime from "../common/dateTime/dateTime.js";
import ProfileRepository from "../../repository/ProfileRepository";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import { store } from "../../store/index";

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
