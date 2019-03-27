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
    };
  },

  components: {
    PersonalPhotos
  },



  methods: {}
  
};
</script>
