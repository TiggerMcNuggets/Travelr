
<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-layout column align-center text-sm-center>
    <v-flex>
      <div class="profile-container">
        <img class="profile-image" :src="getImgUrl" />
      </div>

      <div class="profile-heading-section">
        <h3 class="headline font-weight-light traveller-name">{{fname}} {{mname}} {{lname}}</h3>
        <p class="caption font-weight-light">{{traveller.email}}</p>
      </div>
    </v-flex>

    <v-container fluid>
      <v-layout row wrap align-center>
        <v-flex col md6>
          <div class="field-section">
            <p class="caption font-weight-light field-title">DOB</p>
            <v-divider></v-divider>
            <p class="body-1 font-weight-normal">{{dob}}</p>
          </div>

          <div class="field-section">
            <p class="caption font-weight-light field-title">TRAVELLER TYPES</p>
            <v-divider></v-divider>
            <ul class="chip-list">
              <li v-for="type in types" :value="type.value" :key="type.value">
                <v-chip>{{type.name}}</v-chip>
              </li>
            </ul>
          </div>
        </v-flex>
        <v-flex col md6>
          <div class="field-section">
            <p class="caption font-weight-light field-title">GENDER</p>
            <v-divider></v-divider>
            <p class="body-1 font-weight-normal">{{gender}}</p>
          </div>

          <div class="field-section">
            <p class="caption font-weight-light field-title">NATIONALITIES</p>
            <v-divider></v-divider>
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
          </div>
        </v-flex>
      </v-layout>
    </v-container>
  </v-layout>
</template>

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