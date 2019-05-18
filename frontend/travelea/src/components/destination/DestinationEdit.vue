
/* eslint-disable */

<template>
  <v-card>
    <div class="outer-container">
      <v-form ref="form" lazy-validation>
        <div class="container">
          <div class="section">
            <div class="dest-name">
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
              <h2 class="headline">Edit Destination</h2>
            </div>
            <div>
              <v-btn
                class="upload-toggle-button"
                fab
                small
                dark
                color="indigo"
                @click="toggleShowUploadPhoto"
                v-if="isMyProfile || isAdminUser"
              >
                <v-icon dark>add</v-icon>
              </v-btn>
            </div>
          </div>
          <v-divider class="photo-header-divider"></v-divider>
          <v-layout>
            <v-flex xs12 md6>
              <v-text-field
                v-model="destination.name"
                :rules="nameRules"
                :counter="10"
                label="Destination Name"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs12 md6>
              <v-text-field
                v-model="destination.type"
                :rules="nameRules"
                :counter="10"
                label="Destination Type"
                required
              ></v-text-field>
            </v-flex>
          </v-layout>

          <v-layout>
            <v-flex xs12 md6>
              <v-text-field
                v-model="destination.district"
                :rules="nameRules"
                :counter="10"
                label="Destination District"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs12 md6>
              <v-text-field
                v-model="destination.country"
                :rules="nameRules"
                :counter="10"
                label="Country"
                required
              ></v-text-field>
            </v-flex>
          </v-layout>

          <v-layout>
            <v-flex xs12 md6>
              <v-text-field
                v-model.number="destination.latitude"
                :rules="numberRules"
                label="Latitude"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs12 md6>
              <v-text-field
                v-model.number="destination.longitude"
                :rules="numberRules"
                label="Longitude"
                required
              ></v-text-field>
            </v-flex>
          </v-layout>
          <div class="buttons-div">
            <v-btn color="red" @click="routeBackToPrevPage">CANCEL</v-btn>
            <v-btn @click="updateDestination">UPDATE DESTINATION</v-btn>
            <v-btn @click="undo" :disabled="!mCanUndo()">UNDO</v-btn>
            <v-btn @click="redo" :disabled="!mCanRedo()">REDO</v-btn>
          </div>
        </div>
        <v-alert :value="isError" type="error">This destination is already available to you</v-alert>
      </v-form>
    </div>
  </v-card>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.buttons-div {
  margin-top: 2em;
}

.container {
  align-self: center;
  display: inline-block;
  text-align: left;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import { rules } from "../form_rules";
import { store } from "../../store/index";
import RollbackMixin from "../mixins/RollbackMixin.vue";

export default {
  mixins: [RollbackMixin],
  data() {
    return {
      destination: {},
      isError: false,
      isMyProfile: false,
      isAdminUser: false,
      ...rules
    };
  },
  methods: {

    setDestination: function() {
      destinationRepository
        .getDestination(this.$route.params.id, this.$route.params.dest_id)
        .then(result => {
          this.destination = result.data;
          this.mSetPreviousBody(result.data);

          this.isMyProfile = store.getters.getUser.id === this.$route.params.id;
          this.isAdminUser = store.getters.getIsUserAdmin;
        });
    },

    /**
     * Updates a destination by through a request to the API based on the updated data in the form.
     * This function will first check if the data is valid and only submit successfully if it is.
     */
    updateDestination: function() {
      if (this.$refs.form.validate()) {
        const userId = this.$route.params.id;
        const destId = this.$route.params.dest_id;
        destinationRepository
          .updateDestination(
            userId, 
            destId,
            this.destination
          )
          .then(() => {
            const url = `/users/${userId}/destinations/${destId}`;  
            this.mCheckpoint(
              'PUT',
              {
                url: url,
                body: this.destination
              },
              {
                url: url,
                body: this.mPreviousBody
              }
            );
            this.mSetPreviousBody(this.destination);
            this.isError = false;
          })
          .catch((e) => {
            console.error(e);
            this.isError = true;
          });
      }
    },

    undo: function() {
      const actions = [this.setDestination];
      this.mUndo(actions); 
    },

    redo: function() {
      const actions = [this.setDestination];
      this.mRedo(actions);
    },

    /**
     * Redirects the user back to the previous page.
     */
    routeBackToPrevPage: function() {
      this.$router.go(-1); // sends you back to the previous page
    }
  },

  /**
   * Gets the current destination data to display in the form to update on component creation.
   */
  created: function() {
    this.setDestination();
  }
};
</script>
