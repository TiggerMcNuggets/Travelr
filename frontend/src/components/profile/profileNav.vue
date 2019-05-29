/* eslint-disable */

<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-card class="profile-sidebar">
    <div class="profile-top">
      <!-- <v-avatar size="100%" class="profile-photo"> -->
      <div class="profile-container">
        <img class="profile-image" :src="getImgUrl">
      </div>

      <v-btn
        v-if="isMyProfile || isAdminUser"
        @click="goToEdit(id)"
        class="profile-edit-button"
        fab
        small
        dark
        color="indigo"
      >
        <v-icon dark>edit</v-icon>
      </v-btn>
    </div>

    <div>
      <div class="profile-heading-section">
        <h3 class="headline font-weight-light traveller-name">{{fname}}</h3>
        <p class="caption font-weight-light">{{email}}</p>
      </div>

      <div class="field-section duel-section">
        <div class="field-section">
          <p class="body-1 font-weight-normal">{{dob}}</p>
          <p class="caption font-weight-light field-title">DOB</p>
        </div>
           <v-divider></v-divider>
        <div class="field-section">
          <p class="body-1 font-weight-normal">{{gender}}</p>
          <p class="caption font-weight-light field-title">GENDER</p>
        </div>
      </div>
      <v-divider></v-divider>

      <div class="field-section">
        <ul class="chip-list">
          <li v-for="type in nationalities" :value="type.value" :key="type.value">
            <div v-if="!type.old">
              <v-chip class="nationality">{{type.name}}</v-chip>
            </div>
            <div v-else>
              <v-chip color="amber lighten-3" class="nationality">
                {{type.name}}
                <v-tooltip top>
                  <template v-slot:activator="{ on }">
                    <v-avatar v-on="on">
                      <v-icon color="gray" right>info</v-icon>
                    </v-avatar>
                  </template>
                  <span>Outdated Nationality</span>
                </v-tooltip>
              </v-chip>
            </div>
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
  text-decoration: none;
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

.profile-top {
  display: flex;
  justify-content: center;
}

li {
  list-style: none;
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

.profile-image {
  height: 120px !important;
}

.profile-container {
  height: 120px !important;
  width: 120px !important;
  overflow: hidden !important;
  border-radius: 120px !important;
}

.profile-nav {
  border: 1px solid black;
}
</style>


<script>
import { store } from "../../store/index";
import DefaultPic from "../../assets/defaultPic.png";
import base_url from "../../repository/BaseUrl";

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
      isAdminUser: false,
      isMyProfile: false,
      id: this.$route.params.id
    };
  },

  /**
   * Watches change of the the id param in the url to update data of the user on the sidebar accordingly.
   */
  watch: {
    "$route.params.id": function() {
      this.init();
    }
  },

  /**
   * Initialises the component on creation.
   */
  created: function() {
    this.init();
  },

  methods: {
    /**
     * Initialises the applicaiton by getting the traveller and checking their authority for the page being displayed.
     */
    init() {
      this.traveller = store.getters.getUser;
      this.checkIfProfileOwner();
      this.isAdminUser = store.getters.getIsUserAdmin;
    },

    /**
     * Checks if the user is viewing their own page.
     */
    checkIfProfileOwner() {
      this.id = this.$route.params.id;
      this.isMyProfile = store.getters.getUser.id == this.id;
    },

    /**
     * Redirects the user to the profile edit page.
     */
    goToEdit() {
      this.$router.push("/user/" + this.id + "/edit");
    }
  },
  computed: {
    /**
     * Gets the src of the profile picture
     * @returns {string} The src of the profile picture.
     */
    getImgUrl() {
      if (!this.profilePic || this.profilePic == "defaultPic.png") {
        return DefaultPic;
      } else {
        return base_url + "/api/travellers/profile-photo/" + this.id;
      }
    }
  }
};
</script>