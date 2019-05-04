/* eslint-disable */

<template>
  <v-card class="profile-sidebar">
    <div class="profile-top">
      <!-- <v-avatar size="100%" class="profile-photo"> -->
      <div class="profile-container">
        <img class="profile-image" :src="getImgUrl">
      </div>

      <v-btn
        v-if="isMyProfile || isAdminUser"
        @click="goToEdit(user_id)"
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
import DefaultPic from "../../assets/defaultPic.png"

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

   watch: {
    '$route.params.id': function() {
      this.init();
    }
  },


  created: function() {
    this.traveller = store.getters.getUser;
    this.checkIfProfileOwner();
    this.isAdminUser = store.getters.getIsUserAdmin;
  },
  methods: {
      init() {
      this.traveller = store.getters.getUser;
      this.checkIfProfileOwner();
      this.isAdminUser = store.getters.getIsUserAdmin;
    },
    checkIfProfileOwner() {
      this.id = this.$route.params.id;
      this.isMyProfile = (store.getters.getUser.id == this.id);
    },
    goToEdit(id) {
      this.$router.push("/user/"+this.id+"/edit")
    }

  },
  computed: {
    
    getImgUrl() {
      if (!this.profilePic || this.profilePic == 'defaultPic.png') {
        return DefaultPic;
      } else {
        return "http://localhost:9000/api/travellers/profile-photo/" + this.id;
      }
    }
  }
};
</script>