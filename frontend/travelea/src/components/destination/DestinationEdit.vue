/* eslint-disable */

<template>
  <div class="outer-container">
    <div class="banner">
      <h1>EDIT DESTINATION</h1>
      <hr>
    </div>

    <v-form ref="form" lazy-validation>
      <div class="container">
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
              v-model="destination.destination_type"
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
              v-model.number="destination.crd_latitude"
              :rules="numberRules"
              label="Latitude"
              required
            ></v-text-field>
          </v-flex>

          <v-flex xs12 md6>
            <v-text-field
              v-model.number="destination.crd_longitude"
              :rules="numberRules"
              label="Longitude"
              required
            ></v-text-field>
          </v-flex>
        </v-layout>
        <div class="buttons-div">
          <v-btn color="red" class="update-button" v-on:click="routeBackToPrevPage">CANCEL</v-btn>
          <v-btn class="update-button" v-on:click="updateDestination">UPDATE DESTINATION</v-btn>
        </div>
      </div>
    </v-form>
  </div>
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
  margin: 10px 100px;
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
import {
  getOneDestination,
  updateDestination
} from "../../repository/DestinationEditRepository";
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
        updateDestination(this.$route.params.id, this.destination).then(() => {
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
    getOneDestination(this.$route.params.id).then(result => {
      this.destination = result;
    });
  }
};
</script>
