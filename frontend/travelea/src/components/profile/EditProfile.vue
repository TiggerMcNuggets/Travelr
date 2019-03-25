<template>
    <v-form ref="form" v-model="isValid" lazy-validation>
        <v-flex lg6 offset-lg3 sm8 offset-sm2 text-xs-center>
            <v-card class="profile-card">

                <TravellerForm 
                    :fname.sync="traveller.firstName" 
                    :mname.sync="traveller.middleName" 
                    :lname.sync="traveller.lastName" 
                    :dob.sync="dateOfBirth" 
                    :gender.sync="traveller.gender" 
                    :types.sync="traveller.travellerTypes" 
                    :nationalities.sync="nationalities" 
                    :passports.sync="passports" />

                <v-btn :disabled="!isValid" color="primary" @click="handleEdit">
                    Save
                </v-btn>

            </v-card>
        </v-flex>
    </v-form>
</template>

<style>
.profile-card {
    margin-top: 20px;
}

</style>

<script>
import TravellerForm from "../common/travellerForm/TravellerForm";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import dateTime from "../common/dateTime/dateTime.js";
import ProfileRepository from "../../repository/ProfileRepository";

import {store} from "../../store/index";

export default {
  name: "EditProfile",
  components: { TravellerForm }, 
  store,
  data() {
    return {
      isValid: false,

      traveller: {},

      dateOfBirth: "",
      nationalities: [],
      passports: [],
    };
  },
  mounted() {
    this.getTraveller();
  },
  methods: {
    getTraveller() {
      let user = store.getters.getUser;
      this.traveller = user.profile;
      this.setTravellerToFields();
      delete this.traveller.email;
      delete this.traveller.id;
    },

    setTravellerToFields() {
      [this.nationalities, this.passports] = travellerFormHelper.convertFromNationalitiesRes(this.traveller.nationalities);
      this.traveller.travellerTypes = travellerFormHelper.convertFromTravellerTypesRes(this.traveller.travellerTypes)
      this.dateOfBirth = dateTime.convertTimestampToString(this.traveller.dateOfBirth);
      
    },

    setFieldsToTraveller() {
      this.traveller.nationalities = travellerFormHelper.convertToNationalitiesReq(this.nationalities, this.passports);
      this.traveller.dateOfBirth = dateTime.convertStringToTimestamp(this.dateOfBirth);
    },

    async handleEdit() {
      if (this.$refs.form.validate()) {
        this.setFieldsToTraveller();
        let id = store.getters.getId;

        store.dispatch("updateUser", this.traveller)
        .then((response) => {
          this.$router.push("/profile");
        })
        .catch((e) => {
            console.log(e);
        })
        
      }
     },
  },
};
</script>
