
<template>
  <v-container fluid>
    <PageHeader title="Profile" disableUndoRedo :options="options" />
    <ProfileDetails
      :fname.sync="traveller.firstName"
      :mname.sync="traveller.middleName"
      :lname.sync="traveller.lastName"
      :email:sync="traveller.email"
      :dob.sync="dateOfBirth"
      :gender.sync="traveller.gender"
      :types.sync="traveller.travellerTypes"
      :nationalities.sync="nationalities"
      :passports.sync="passports"
      :profilePic.sync="traveller.userProfilePhoto"
    />
  </v-container>
</template>

<script>
import ProfileDetails from "./ProfileDetails";
import dateTime from "../common/dateTime/dateTime.js";
import UserRepository from "../../repository/UserRepository";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import { store } from "../../store/index";
import PageHeader from "../common/header/PageHeader";

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
    ProfileDetails,
    PageHeader
  },

  computed: {
    /**
     * Options used in the header component.
     */
    options() {
      return [{ action: this.goToEdit, icon: "edit" }];
    }
  },

  watch: {
    "$route.params.id": function() {
      this.init();
    }
  },

  created: function() {
    this.init();
  },

  methods: {
    /**
     * Initialises the id and the user data to be displayed on the profile page.
     */
    init() {
      this.id = this.$route.params.id
        ? this.$route.params.id
        : store.getters.getUser.id;

      // Gets user data
      UserRepository.getUser(this.id)
        .then(response => {
          this.traveller = response.data;
          this.setTravellerToFields();
          this.isMyProfile = store.getters.getUser.id == this.id;
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Gets traveller data and sets these the the component fields.
     */
    getTraveller() {
      this.traveller = store.getters.getUser;
      this.setTravellerToFields();
    },

    /**
     * Redirects the user to the profile edit page.
     */
    goToEdit() {
      this.$router.push("/user/" + this.id + "/edit");
    },

    /**
     * Sets the traveller to the component fields of the component.
     */
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
