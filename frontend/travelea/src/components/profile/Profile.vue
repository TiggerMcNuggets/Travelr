/* eslint-disable */

<template>
  <div class="profile-outer-container">
    <div class="profile-inner-container">
      <v-layout row>        
        <aside>          
          <ProfileNav
            :fname.sync="traveller.firstName"
            :mname.sync="traveller.middleName"
            :lname.sync="traveller.lastName"
            :dob.sync="dateOfBirth"
            :gender.sync="traveller.gender"
            :types.sync="traveller.travellerTypes"
            :nationalities.sync="nationalities"
            :passports.sync="passports"
            :profilePic.sync="traveller.userProfilePhoto"
          />
        </aside>
        <main class="profile-main">
          <router-view></router-view>
        </main>
      </v-layout>
    </div>
  </div>
</template>


<style>
.trips-tile {
  margin-left: 20px;
}

.destinations-tile {
  margin-top: 20px;
  height: 290px;
}

.photos-tile {
  height: 300px;
}

aside {
  width: 25%;
  margin-right: 20px;
}

main {
  margin-left: 20px;
}

.profile-inner-container {
  width: 100%;
  margin: 20px;
}

.profile-outer-container {
  text-align: center;
  display: flex;
  justify-content: center;
}

.profile-main {
  width: 100%;
  margin: 0px;
}
</style>


<script>
import ProfileNav from "./profileNav";
import dateTime from "../common/dateTime/dateTime.js";
import UserRepository from "../../repository/UserRepository";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import { store } from "../../store/index";

export default {
  name: "Profile",
  store,

  data() {
    return {
      traveller: {},
      dateOfBirth: "",
      nationalities: [],
      passports: [],
      isMyProfile: false
    };
  },

  components: {
    ProfileNav,
  },
  watch: {
    '$route.params.id': function() {
      this.init();
    }
  },
  created: function() {
    this.init();
  },
  methods: {
    init() {
      let id = this.$route.params.id;
      
      if(!id) { 
        id = store.getters.getUser.id
      }
      UserRepository.getUser(id)
        .then(response => {
          this.traveller = response.data
          this.setTravellerToFields(); 
          this.isMyProfile = (store.getters.getUser.id == id)
        })    
        .catch(err => {
          console.log(err);
        })
    },
    getTraveller() {
      this.traveller = store.getters.getUser;
      this.setTravellerToFields();
    },

    setTravellerToFields() {
      [
        this.nationalities,
        this.passports
      ] = travellerFormHelper.convertFromNationalitiesRes(
        this.traveller.nationalities
      );

      this.dateOfBirth = dateTime.convertTimestampToString(
        this.traveller.dateOfBirth
      );
    }    
  }
};
</script>
