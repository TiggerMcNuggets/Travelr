<template>
  <v-form ref="form" v-model="isValid" lazy-validation>
    <v-flex>
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

      <v-btn :disabled="!isValid" color="primary" @click="handleSignup" ma-0>Sign Up</v-btn>
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

import { store } from "../../store/index";

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
      confirmPassword: ""
    };
  },
  methods: {
    setTraveller() {
      this.$set(
        this.traveller,
        "nationalities",
        travellerFormHelper.convertToNationalitiesReq(
          this.nationalities,
          this.passports
        )
      );
      this.$set(
        this.traveller,
        "dateOfBirth",
        dateTime.convertStringToTimestamp(this.dateOfBirth)
      );
    },

    handleSignup() {
      if (this.$refs.form.validate()) {
        this.setTraveller();
        store
          .dispatch("signup", this.traveller)
          .then(() => {
            let loginInfo = {
              email: this.traveller.email,
              password: this.traveller.password
            };
            return store.dispatch("login", loginInfo);
          })
          .then(() => {
            this.$router.push("/");
          })
          .catch(err => {
            console.log(err);
            this.emailAlert = true;
          });
      }
    }
  }
};
</script>
