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
            <destination></destination>
          </v-card>
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
  margin: 0px;
}
</style>


<script>
import ProfileNav from "./profileNav";
import dateTime from "../common/dateTime/dateTime.js";
import Destination from "../destination/Destination";
import { store } from "../../store/index";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";

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
    Destination
  },

  /**
   * Gets traveller data on mount of the component.
   */
  mounted() {
    this.getTraveller();
  },

  methods: {
    /**
     * Gets the traveller data.
     */
    getTraveller() {
      let user = store.getters.getUser;
      this.traveller = user.profile;
      this.setTravellerToFields();
    },

    /**
     * Sets the traveller data to the fields of the component.
     */
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
