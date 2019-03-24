<template>
    <v-form ref="form" v-model="isValid" lazy-validation>
        <v-flex lg6 offset-lg3 sm8 offset-sm2 text-xs-center>
            <v-card class="signup-card">

                <TravellerForm 
                    :fname.sync="traveller.firstName" 
                    :mname.sync="traveller.middleName" 
                    :lname.sync="traveller.lastName" 
                    :dob.sync="dateOfBirth" 
                    :gender.sync="traveller.gender" 
                    :types.sync="traveller.travellerTypes" 
                    :nationalities.sync="nationalities" 
                    :passports.sync="passports" />

                <SignupFields 
                    :email.sync="traveller.email" 
                    :password.sync="traveller.password" 
                    :confirmPassword.sync="confirmPassword" />

                <v-btn :disabled="!isValid" color="primary" @click="handleSignup">
                    Sign Up
                </v-btn>
            </v-card>
            <v-alert class="email-alert" :value="emailAlert" color="error">Email already taken</v-alert>
        </v-flex>
    </v-form>
</template>

<style>
.signup-card {
    margin-top: 20px;
}
.email-alert {
    display: block;
    margin-top: 10px;
}
</style>

<script>
import TravellerForm from "../common/travellerForm/TravellerForm";
import SignupFields from "./SignupFields";

import travellerFormHelper from "../common/travellerForm/travellerFormHelper.js";
import dateTime from "../common/dateTime/dateTime.js";
import AuthRepository from "../../repository/AuthRepository";

import {store} from "../../store/index";

export default {
  name: "Signup",
  components: { TravellerForm, SignupFields }, 
  store,
  data() {
    return {
      isValid: false,
      emailAlert: false,
      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: [],
      confirmPassword: "",
    };
  },
  methods: {
    setTraveller() {
      this.$set(this.traveller, "nationalities", travellerFormHelper.convertToNationalitiesReq(this.nationalities, this.passports));
      this.$set(this.traveller, "dateOfBirth", dateTime.convertStringToTimestamp(this.dateOfBirth));
    },

    async signup() {
      await store.dispatch("signup", this.traveller);
      const id = store.getters.getUser.id;
      console.log(id);
      if (!id) {
        this.emailAlert = true;
      }
      return id
      
    },
    async login() {
      const loginData = {
        email: this.traveller.email,
        password: this.traveller.password,
      }
      await store.dispatch("login", loginData);
      const token = store.getters.getUser.token;
      localStorage.setItem("token", token);
      console.log(localStorage.getItem("token"));
    },
    async handleSignup() {
      if (this.$refs.form.validate()) {
        this.setTraveller();
        
        if (await this.signup()) {
          await this.login();
          this.$router.push("/profile");
        }
      }
    },
  },
};
</script>
