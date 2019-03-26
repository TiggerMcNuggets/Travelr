<template>
  <v-card>
    <personalPhotos></personalPhotos>
  </v-card>
</template>


<style>

</style>


<script>
import ProfileNav from "./profileNav";
import PersonalPhotos from "./PersonalPhotos2";
import dateTime from "../common/dateTime/dateTime.js";
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
//Trips,
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
