<template>
  <v-form ref="form" v-model="isValid" lazy-validation>
    <v-flex text-xs-left>
      <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
        <v-icon dark>keyboard_arrow_left</v-icon>
      </v-btn>
      <v-card class="profile-card">
        <TravellerForm
          :fname.sync="traveller.firstName"
          :mname.sync="traveller.middleName"
          :lname.sync="traveller.lastName"
          :dob.sync="dateOfBirth"
          :gender.sync="traveller.gender"
          :types.sync="travellerTypes"
          :nationalities.sync="nationalities"
          :passports.sync="passports"
        />

        <div class="upload-section">
          <h3>Upload User Profile Photo</h3>
          <label>
            <input
              class="choose-file-button"
              type="file"
              id="file"
              ref="file"
              v-on:change="handleFileUpload()"
            >
          </label>
          <v-btn v-on:click="submitFile()">Upload Photo</v-btn>
          <v-btn v-on:click="uploadExisting()">Upload Existing</v-btn>
        </div>
        <v-btn :disabled="!isValid" color="primary" @click="handleEdit">Save</v-btn>


         <v-dialog v-model="dialog">
            <PhotoSelect v-bind="{closeDialogue}"/>
        </v-dialog>
        
      </v-card>
    </v-flex>
  </v-form>
</template>

<style>

.upload-section {
  padding: 30px 10px;
}

.choose-file-button {
  background-color: #f5f5f5;
  color: rgba(0, 0, 0, 0.87);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;

  align-items: center;
  border-radius: 2px;
  display: inline-flex;
  height: 36px;
  flex: 0 0 auto;
  font-size: 14px;
  font-weight: 500;
  justify-content: center;
  margin: 6px 8px;
  min-width: 88px;
  outline: 0;
  text-transform: uppercase;
  text-decoration: none;
  position: relative;
  vertical-align: middle;
}
.profile-card {
  margin-top: 20px;
}
</style>

<script>
import userRepo from "../../repository/UserRepository";
import TravellerForm from "../common/travellerForm/TravellerForm";
import PhotoSelect from "../photos/PhotoSelect";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import dateTime from "../common/dateTime/dateTime.js";
import {
  getProfilePic,
  uploadProfilePic,
  setProfilePic
} from "../../repository/PersonalPhotosRepository";

import { store } from "../../store/index";
import { storeImage } from "../../repository/PersonalPhotosRepository";

export default {
  name: "EditProfile",
  components: { TravellerForm, PhotoSelect },

  store,
  data() {
    return {
      isValid: false,
      traveller: {},
      dialog: false,
      dateOfBirth: "",
      nationalities: [],
      travellerTypes: [],
      passports: []
    };
  },
  mounted() {
    this.getTraveller();
  },
  methods: {
    // Sets the file property to the new file uploaded
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    uploadExisting() {
      this.dialog = true;
    },
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);

      uploadProfilePic(this.id, formData).then(() => {
        window.location = "/user/" + this.$route.params.id + "/edit";
      });
    },
    getTraveller() {
      userRepo.getUser(this.id).then(result => {
        this.traveller = result.data;
      });
      this.setTravellerToFields();
    },

    setTravellerToFields() {
      [
        this.nationalities,
        this.passports
      ] = travellerFormHelper.convertFromNationalitiesRes(
        this.traveller.nationalities
      );
      this.travellerTypes = travellerFormHelper.convertFromTravellerTypesRes(
        this.traveller.travellerTypes
      );
      this.dateOfBirth = dateTime.convertTimestampToString(
        this.traveller.dateOfBirth
      );
    },

    setFieldsToTraveller() {
      this.traveller.nationalities = travellerFormHelper.convertToNationalitiesReq(
        this.nationalities,
        this.passports
      );
      this.traveller.dateOfBirth = dateTime.convertStringToTimestamp(
        this.dateOfBirth
      );
      this.traveller.travellerTypes = this.travellerTypes;
    },

    closeDialogue() {
      this.dialog = false;
    },

    handleEdit() {
      if (this.$refs.form.validate()) {
        this.setFieldsToTraveller();
        store
          .dispatch("updateUser", this.traveller)
          .then(() => {
            return store.dispatch("fetchMe");
          })
          .then(() => {
            this.$router.push("/profile");
          })
          .catch(e => {
            console.log(e);
          });
      }
    }
  },

  created: function() {
    this.id = this.$route.params.id;

    if (!this.id) {
      this.id = store.getters.getUser.id;
    }
  }
};
</script>
