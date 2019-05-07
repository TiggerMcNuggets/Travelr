
/* eslint-disable */

<template>
  <v-card>
  <div class="outer-container">
    
    <v-form ref="form" lazy-validation>
      <div class="container">

           <div class="section">
        <div class="dest-name">
          <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
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
          <v-btn color="red" v-on:click="routeBackToPrevPage">CANCEL</v-btn>
          <v-btn v-on:click="updateDestination">UPDATE DESTINATION</v-btn>
        </div>
      </div>
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

.update-button {
  margin-top: 1em;
  width: 49%;
}

.container {
  align-self: center;
  display: inline-block;
  text-align: left;
}

.banner {
  height: 300px;
  width: 100%;

  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url("https://gallery.yopriceville.com/var/albums/Backgrounds/Autumn_Landscape_Background.jpg?m=1442666745");
  background-position: center;
}

.banner h1 {
  font-family: "Karla", sans-serif;
  text-align: center;
  color: white;
  padding-top: 60px;
  font-size: 65px;
  font-weight: bold;
}

.banner hr {
  margin: 10px 200px;
  margin-top: 30px;
  color: white;
  opacity: 0.5;
}
</style>


<script>
import {RepositoryFactory} from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import {rules} from "../form_rules";

export default {
  data() {
    return {
      destination: {},
      ...rules
    };
  },
  methods: {
    

    updateDestination: function() {
      if (this.$refs.form.validate()) {
        destinationRepository.updateDestination(this.$route.params.id, this.$route.params.dest_id, this.destination).then(() => {
          this.$refs.form.reset();
          this.routeBackToPrevPage();
        });
      }
    },
    routeBackToPrevPage: function() {
      this.$router.go(-1); // sends you back to the previous page
    }
  },

  created: function() {
    destinationRepository.getDestination(this.$route.params.id, this.$route.params.dest_id).then(result => {
      this.destination = result.data;
    });
  }
};
</script>
