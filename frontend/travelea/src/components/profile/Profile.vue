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
          <router-view></router-view>
        </main>
      </v-layout>
    </div>
  </div>
</template>


<style>

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
  height: 100%;
  margin: 0px;
}
</style>


<script>
import ProfileNav from "./profileNav";
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
  },

  created: function() {
    this.getTraveller();
  },

  methods: {
    getTraveller() {
      this.traveller = store.getters.getUser;
      this.setTravellerToFields();
    },

    setTravellerToFields() {
      [
        this.nationalities,
        this.passports
      ] = travellerFormHelper.convertFromNationalitiesRes(
        this.traveller.nationalities
      );

      this.dateOfBirth = dateTime.convertTimestampToString(
        this.traveller.dateOfBirth
      );
    }
  }
};
</script>
