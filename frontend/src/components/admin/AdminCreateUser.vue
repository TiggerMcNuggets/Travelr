<template>
  <v-form ref="form" v-model="isValid" lazy-validation>
    <undo-redo-buttons
      :canRedo="rollbackCanRedo()"
      :canUndo="rollbackCanUndo()"
      :undo="undo"
      :redo="redo"
    ></undo-redo-buttons>
    <TravellerForm
      :fname.sync="traveller.firstName"
      :mname.sync="traveller.middleName"
      :lname.sync="traveller.lastName"
      :dob.sync="dateOfBirth"
      :gender.sync="traveller.gender"
      :types.sync="traveller.travellerTypes"
      :nationalities.sync="nationalities"
      :passports.sync="passports"
    />

    <SignupFields
      :email.sync="traveller.email"
      :password.sync="traveller.password"
      :confirmPassword.sync="confirmPassword"
    />

    <v-layout wrap>
      <v-flex xs12 sm6 md8 class="margin-left-to-checkbox">
        <v-checkbox v-model="checkbox" :label="'Create user as admin'"></v-checkbox>
      </v-flex>
      <v-flex xs12 sm6 md3>
        <v-btn :disabled="!isValid" color="primary" @click="handleSignup">Create Profile</v-btn>
      </v-flex>
    </v-layout>
    <v-alert class="email-alert" :value="emailAlert" color="error">Email already taken</v-alert>
    <v-alert class="success" :value="createdUser" color="success">User created!</v-alert>
    <v-alert class="success" :value="undoSuccessAlert" color="success">User creation undone!</v-alert>
    <v-alert class="success" :value="redoSuccessAlert" color="success">User creation redone!</v-alert>
  </v-form>
</template>

<style>
.email-alert {
  display: block;
  margin-top: 10px;
}
.margin-left-to-checkbox {
  margin-left: 2em;
}
</style>

<script>
import TravellerForm from "../common/travellerForm/TravellerForm";
import SignupFields from "../signup/SignupFields";
import RollbackMixin from "../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons.vue";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import dateTime from "../common/dateTime/dateTime.js";
import { store } from "../../store/index";

export default {
  name: "AdminCreateUser",
  mixins: [RollbackMixin],

  components: { TravellerForm, SignupFields, UndoRedoButtons },

  store,

  data() {
    return {
      checkbox: true,
      isValid: false,
      emailAlert: false,
      undoSuccessAlert: false,
      redoSuccessAlert: false,
      createdUser: false,
      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: [],
      confirmPassword: ""
    };
  },

  methods: {
    /**
     * Sets all visible alerts to invisible
     */
    clearAlerts() {
      this.emailAlert = false;
      this.createdUser = false;
      this.undoSuccessAlert = false;
      this.redoSuccessAlert = false;
    },

    /**
     * Clears all fields and form validation errors
     */
    clearFields() {
      this.traveller = {};
      this.nationalities = [];
      this.passports = [];
      this.confirmPassword = "";
      this.$refs.form.reset();
    },

    /**
     * Sets the nationalities and the passports lists, formatted date of birth, and whether the admin checkbox should be
     * ticked or not to the current state.
     */
    setTraveller() {
      // Sets the nationalities and passport lists.
      this.traveller.nationalities = travellerFormHelper.convertToNationalitiesReq(
        this.nationalities,
        this.passports
      );

      // Sets the formatted date of birth of the traveller.
      this.traveller.dateOfBirth = dateTime.convertStringToTimestamp(
        this.dateOfBirth
      );

      // Sets the admin text box to selected.
      if (this.checkbox) {
        this.traveller.accountType = 1;
      }
    },

    /**
     * Allows the user to sign up as an admin
     * @returns {Promise<boolean>} Whether the sign up is successful or not.
     */
    async signup() {
      let response;

      // Tries to sign up the user as admin
      try {
        response = await store.dispatch("signupOtherUser", this.traveller);
      } catch (e) {
        console.log(e);
      }

      // Checks if email is taken or not
      if (!response) {
        this.emailAlert = true;
        return false;
      }

      const url = `/travellers/${response.data.id}/toggle_deleted`;
      this.rollbackCheckpoint(
        "POST",
        {
          url: url
        },
        {
          url: url
        }
      );
      this.clearFields();
      return true;
    },

    /**
     * Checks form validation and creates the user
     */
    async handleSignup() {
      this.clearAlerts();
      if (this.$refs.form.validate()) {
        this.setTraveller();

        if (await this.signup()) {
          this.createdUser = true;
        }
      }
    },

    /**
     * Undoes the last action
     */
    undo: function() {
      const actions = [
        this.clearAlerts,
        () => {
          this.undoSuccessAlert = true;
        }
      ];
      this.rollbackUndo(actions);
    },

    /**
     * Redoes the last action
     */
    redo: function() {
      const actions = [
        this.clearAlerts,
        () => {
          this.redoSuccessAlert = true;
        }
      ];
      this.rollbackRedo(actions);
    }
  }
};
</script>
