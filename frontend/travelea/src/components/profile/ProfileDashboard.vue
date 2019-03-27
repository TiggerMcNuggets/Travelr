<template>
  <v-layout row class="dashboard">
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
        <router-link to="/destinations">
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
      <v-layout column >
        <v-flex d-flex >
          <router-link to="/profile/trips">
            <v-card d-flex class="trips-tile">
              <v-img
                src="https://cdn.vuetifyjs.com/images/cards/desert.jpg"
                aspect-ratio="2.75"
              ></v-img>
              <h5>Trips</h5>
            </v-card>
          </router-link>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<style>
.trips-tile {
    height: 100%;
}
.dashboard h5 {
    font-size: 2em;
    text-decoration: none !important;
    text-align: center;
}

</style>


<script>

import Trips from "../trips/Trips";
// import PersonalPhotos from "./PersonalPhotos2";

export default {
  name: "ProfileDashboard",


  data() {
    return {
      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: []
    };
  },

  components: {
    Trips,
    // PersonalPhotos
  } 
};

</script>
