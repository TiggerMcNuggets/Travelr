<template>
  <v-container fluid ref="main_dashboard" ma-0>
    <v-layout row wrap fill-height>
      <v-flex col xs12 md5>
        <div class="photos-tile" ref="photo_tile">
          <v-card class="tile" @click="goToUserPhotos(user_id)">
            <div>
              <v-img
                src="https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?cs=srgb&dl=background-calm-clouds-747964.jpg&fm=jpg"
                :height="halfImageHeight"
              >
                <v-card-title class="fill-height align-start tile-overlay">
                  <h2
                    v-if="isMyProfile"
                    class="headline font-weight-light tile-heading text--white"
                  >Media</h2>
                  <h2 v-else class="font-weight-light tile-heading">User Photos</h2>
                </v-card-title>
              </v-img>
            </div>
          </v-card>
        </div>

        <div class="destinations-tile">
          <v-card
            class="tile"
            v-if="isMyProfile || $store.getters.getIsUserAdmin"
            @click="goToUserDesinations(user_id)"
          >
            <v-img
              class="tile-image"
              :height="halfImageHeight"
              src="https://www.rd.com/wp-content/uploads/2017/11/this-is-the-one-destination-people-want-to-visit-before-they-die-hint-its-not-in-europe_458190886_maria-savenko-1024x683.jpg"
            >
              <v-card-title class="fill-height align-end tile-overlay">
                <h2 v-if="isMyProfile" class="headline font-weight-light tile-heading">Destinations</h2>
                <h2 v-else class="font-weight-light tile-heading">User Destinations</h2>
              </v-card-title>
            </v-img>
          </v-card>
        </div>
      </v-flex>

      <v-flex col xs12 md7>
        <div ref="trip_tile">
          <v-card class="trips_tile tile" @click="goToUserTrips(user_id)">
            <v-img
              src="https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1353&q=80"
              :height="fullImageHeight"
              class="tile-image"
            >
              <v-card-title class="fill-height align-end tile-overlay">
                <h2 v-if="isMyProfile" class="headline font-weight-light tile-heading">Trips</h2>
                <h2 v-else class="headline font-weight-light tile-heading">User Trips</h2>
              </v-card-title>
            </v-img>
          </v-card>
        </div>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { store } from "../../store/index";

export default {
  name: "ProfileDashboard",
  data() {
    return {
      traveller: {},
      dateOfBirth: "",
      nationalities: [],
      passports: [],
      isMyProfile: false,
      user_id: this.$route.params.id,
      halfImageHeight: 0,
      fullImageHeight: 0
    };
  },
  watch: {
    "$route.params.id": function() {
      this.init();
    }
  },

  methods: {
    /**
     * Checks if the user signed in matches the profile being looked at.
     */
    init() {
      this.checkIfProfileOwner();
    },

    /**
     * Redirects the user to the destinations page.
     * @param id The id of the user
     */
    goToUserDesinations(id) {
      const endpoint = "/user/" + id + "/destinations";
      this.$router.push(endpoint);
    },

    /**
     * Returns whether the signed in user is the looking at their own page.
     */
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.isMyProfile = store.getters.getUser.id == id;
    },

    /**
     * Redirects the user to the trips page.
     * @param id The id of the user
     */
    goToUserTrips(id) {
      const endpoint = "/user/" + id + "/trips";
      this.$router.push(endpoint);
    },

    /**
     * Redirects the user to the photos page.
     * @param id
     */
    goToUserPhotos(id) {
      const endpoint = "/user/" + id + "/media";
      this.$router.push(endpoint);
    },

    /**
     * Sets the height of the images for the dashboard which is derived from the window height and subtracting margins.
     */
    updateDashboardHeight() {
      this.fullImageHeight = window.innerHeight - 114;
      this.halfImageHeight = (window.innerHeight - 134) / 2;
    }
  },

  /**
   * Sets the initial heights of the images and sets this to update on window resize
   */
  mounted() {
    this.updateDashboardHeight();
    window.addEventListener("resize", this.updateDashboardHeight);
  },

  /**
   * Removes the resize event
   */
  beforeDestroy: function() {
    window.removeEventListener("resize", this.updateDashboardHeight);
  },

  /**
   * Initialises the component on creation.
   */
  created: function() {
    this.init();
  }
};
</script>
