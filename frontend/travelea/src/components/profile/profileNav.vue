/* eslint-disable */

<template>
  <v-card class="profile-sidebar">
    <div>
      <v-avatar size="100px" class="profile-photo">
        <img
            :src="getImgUrl"
        >
      </v-avatar>
      <router-link v-if="isMyProfile" to="/profile/edit">
        <v-btn class="profile-edit-button" fab small dark color="indigo">
          <v-icon dark>edit</v-icon>
        </v-btn>
      </router-link>
    </div>

    <div>
      <div class="profile-heading-section">
        <h3 class="headline font-weight-light traveller-name">{{fname}}</h3>
        <p class="caption font-weight-light">{{email}}</p>
      </div>
      <v-divider></v-divider>

      <div class="field-section duel-section">
        <div class="field-section">
          <p class="body-1 font-weight-normal">{{dob}}</p>
          <p class="caption font-weight-light field-title">DOB</p>
        </div>
        <div class="field-section">
          <p class="body-1 font-weight-normal">{{gender}}</p>
          <p class="caption font-weight-light field-title">GENDER</p>
        </div>
      </div>
      <v-divider></v-divider>

      <div class="field-section">
        <ul class="chip-list">
          <li v-for="type in nationalities" :value="type.value" :key="type.value">
            <v-chip class="nationality">{{type.name}}</v-chip>
          </li>
        </ul>
        <p class="caption font-weight-light field-title">NATIONALITIES</p>
      </div>
      <v-divider></v-divider>

      <div class="field-section">
        <ul class="chip-list">
          <li v-for="type in types" :value="type.value" :key="type.value">
            <v-chip>{{type.name}}</v-chip>
          </li>
        </ul>
        <p class="caption font-weight-light field-title">TRAVELLER TYPES</p>
      </div>
    </div>
    <v-divider></v-divider>
  </v-card>
</template>

<style>
.chip-list {
  display: flex;
  margin-bottom: 20px;
}

a {
    text-decoration:none;
}

.profile-edit-button {
    position: absolute;
    right: 25%;
    top: 100px;
}

.profile-edit-button:hover {
    position: absolute;
    right: 25%;
    top: 100px;
}

.profile-edit-button:active {
    position: absolute;
    right: 25%;
    top: 100px;
}

.profile-edit-button:focus {
    position: absolute;
    right: 25%;
    top: 100px;
}

li {
  list-style: none;
}

.duel-section {
  display: flex;
  justify-content: space-between;
}

.traveller-name {
  font-size: 36px;
  line-height: 0px;
  padding-bottom: 5px;
}

.profile-heading-section {
  margin: 30px 0px;
}

.field-section {
  text-align: left;
}

.field-title {
  line-height: 0px;
  color: grey;
}

.profile-sidebar {
  text-align: center;
  padding: 20px;
  height: 100%;
}

.profile-photo {
  margin: 17px 0px;
  clear: both;
}
.profile-nav {
  border: 1px solid black;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let profileRepository = RepositoryFactory.get("profile");

import { store } from "../../store/index";

export default {
  store,

  props: [
    "fname",
    "mname",
    "lname",
    "dob",
    "gender",
    "types",
    "nationalities",
    "passports",
    "email",
    "profilePic"
  ],

  data() {
    return {
      profile: {},
      isMyProfile: false,
      user_id: this.$route.params.id
    };
  },
  created: function() {
    this.traveller = store.getters.getUser;
    this.checkIfProfileOwner();
  },
  methods: {  
    checkIfProfileOwner() {
      let id = this.$route.params.id;
      this.isMyProfile = (store.getters.getUser.id == id || id == undefined);
    }
  },
  computed: {
      getImgUrl(place = "somewhere else") {
      console.log(this.profilePic);
      if(!this.profilePic) {
        return require("../../../../../backend/resources/images/defaultPic.png")
      } else {
        return require("../../../../../backend/resources/images/" +
                this.profilePic);
      }
      
    }
  },

};
</script>
