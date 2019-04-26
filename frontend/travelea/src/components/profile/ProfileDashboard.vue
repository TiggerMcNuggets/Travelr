<template>
  <v-layout row class="dashboard">
    <v-flex d-flex xs4 order-xs5>
      <v-layout column>
        <div @click="goToUserPhotos(user_id)">
         <div >
          <v-card d-flex class="photos-tile tile">
            <v-img
              src="https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?cs=srgb&dl=background-calm-clouds-747964.jpg&fm=jpg"
              class="tile-image"
            ></v-img>
            <h2 v-if="isMyProfile" class="headline font-weight-light tile-heading">My Photos</h2>
            <h2 v-else class="headline font-weight-light tile-heading">User Photos</h2>
          </v-card>
           </div>
        </div>
        <div v-if="isMyProfile" @click="goToUserDesinations(user_id)">
         <div>
          <v-card d-flex class="destinations-tile tile">
            <v-img
              class="tile-image"
              src="https://www.rd.com/wp-content/uploads/2017/11/this-is-the-one-destination-people-want-to-visit-before-they-die-hint-its-not-in-europe_458190886_maria-savenko-1024x683.jpg"
            ></v-img>
            <h2 v-if="isMyProfile" class="headline font-weight-light tile-heading">My Destinations</h2>
            <h2 v-else class="headline font-weight-light tile-heading">User Destinations</h2>
          </v-card>
          </div>
        </div>
      </v-layout>
    </v-flex>
    <v-flex d-flex x8 order-xs5>
      <v-layout column>
        <v-flex d-flex>
          <div @click="goToUserTrips(user_id)">
            <v-card d-flex class="trips-tile tile">
        
              <v-img
                src="https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1353&q=80"
                class="tile-image"
              ></v-img>
              
              <h2 v-if="isMyProfile" class="headline font-weight-light tile-heading">My Trips</h2>
              <h2 v-else class="headline font-weight-light tile-heading">User Trips</h2>
            </v-card>
          </div>
        </v-flex>
      </v-layout>
    </v-flex>
  </v-layout>
</template>

<style>

.trips-tile {
  margin-left: 20px;
}

.destinations-tile {
  margin-top: 20px;
  height: 100%;
}

.photos-tile {
  height: 100%;
}

.tile:hover {
  opacity: 0.8;
}

.tile-heading {
  text-decoration: none !important;
  padding: 40px 20px;
  font-size: 30px;
}

.tile-image {
  height: 80%;
}

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
import UserRepository from "../../repository/UserRepository";
import Trips from "../trips/Trips";
import { store } from "../../store/index";
// import PersonalPhotos from "./PersonalPhotos2";

export default {
  name: "ProfileDashboard",

  data() {
    return {
      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: [],
      isMyProfile: false,
      user_id: this.$route.params.id
    };
  },
  methods: {
    goToUserDesinations(id) {
        var endpoint = '/user/' + id + '/destinations'
        this.$router.push(endpoint)
    },
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.isMyProfile = (store.getters.getUser.id == id);
    },
    goToUserTrips(id) {
        var endpoint = '/user/' + id + '/trips'
        this.$router.push(endpoint)
    },
    goToUserPhotos(id) {
        var endpoint = '/user/' + id + '/photos'
        this.$router.push(endpoint)
    }
  },
  created: function() {
    this.checkIfProfileOwner();
  },

  components: {
    Trips
    // PersonalPhotos
  }
};
</script>
