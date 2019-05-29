<template>
  <v-form ref="form" v-model="isValid" lazy-validation>
    <v-flex text-xs-left>
      <v-card class="profile-card">
        <v-btn class="upload-toggle-button" fab small dark color="indigo" @click="$router.go(-1)">
          <v-icon dark>keyboard_arrow_left</v-icon>
        </v-btn>
        <UndoRedoButtons
                :canRedo="rollbackCanRedo()"
                :canUndo="rollbackCanUndo()"
                :undo="undo"
                :redo="redo"></UndoRedoButtons>

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
          <v-btn @click="submitFile">Upload Photo</v-btn>
        </div>
        <v-btn :disabled="!isValid" color="primary" @click="handleEdit">Save</v-btn>
        <v-alert :value="editErrorAlert" type="error">
          Cannot edit profile
        </v-alert>
        <v-alert :value="undoRedoErrorAlert" type="error">
          Cannot undo or redo
        </v-alert>
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
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import dateTime from "../common/dateTime/dateTime.js";
import { uploadProfilePic } from "../../repository/PersonalPhotosRepository";
import RollbackMixin from "../mixins/RollbackMixin.vue";

import { store } from "../../store/index";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons";

export default {
  name: "EditProfile",
  components: {UndoRedoButtons, TravellerForm},

  store,
  mixins: [RollbackMixin],
  data() {
    return {
      isValid: false,
      traveller: {},
      dialog: false,
      dateOfBirth: "",
      nationalities: [],
      travellerTypes: [],
      passports: [],
      editErrorAlert: false,
      undoRedoErrorAlert: false,
    };
  },
  methods: {
    // Sets the file property to the new file uploaded

    /**
     * Sets all visible alerts to invisible
     */
    resetAlerts() {
      this.editErrorAlert = false;
      this.undoRedoErrorAlert = false;
    },

    /**
     * Sets the file selected from the file system which will be uploaded.
     */
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },

    /**
     * Submits the file and uploads it as the users profile pic using the backend endpoint.
     */
    submitFile() {
      let formData = new FormData();
      formData.append("picture", this.file);

      uploadProfilePic(this.id, formData).then(() => {
        window.location = "/user/" + this.$route.params.id + "/edit";
      });
    },

    /**
     * Gets the traveller data which is being edited by using the backend endpoint.
     */
    getTraveller() {
      userRepo.getUser(this.id).then(result => {
        this.traveller = result.data;

        // This is set to later be pushed as a reaction to the rollback stack
        // The double set calls are needed to convert get response object into a put request object
        let previousBody = this.setTravellerToFields({...result.data});
        previousBody = this.setFieldsToTraveller(previousBody)
        this.rollbackSetPreviousBody(previousBody);

        // Convert original object structure to one that will work with the selects
        this.traveller = this.setTravellerToFields(this.traveller);
      });
    },

    /**
     * Sets the nationalities and the passports lists, formatted date of birth of the user being edited.
     */
    setTravellerToFields(traveller) {
      [
        this.nationalities,
        this.passports
      ] = travellerFormHelper.convertFromNationalitiesRes(
        traveller.nationalities
      );
      this.travellerTypes = travellerFormHelper.convertFromTravellerTypesRes(
        traveller.travellerTypes
      );
      this.dateOfBirth = dateTime.convertTimestampToString(
        traveller.dateOfBirth
      );
      return traveller;
    },

    /**
     * Gets the nationalities and the passports lists, formatted date of birth from the current form.
     */
    setFieldsToTraveller(traveller) {
      traveller.nationalities = travellerFormHelper.convertToNationalitiesReq(
        this.nationalities,
        this.passports
      );
      traveller.dateOfBirth = dateTime.convertStringToTimestamp(
        this.dateOfBirth
      );
      traveller.travellerTypes = this.travellerTypes;
      return traveller
    },

    /**
     * Validates the data in the form and if valid updated the users details in the database. Redirects the user back to
     * their profile page.
     */
    handleEdit() {
      this.resetAlerts();
      if (this.$refs.form.validate()) {
        this.traveller = this.setFieldsToTraveller(this.traveller);
        store
          .dispatch("updateUser", this.traveller)
          .then(() => {
            const url = `/travellers/${this.id}`

            this.rollbackCheckpoint(
              'PUT',
              {
                url: url,
                body: {...this.traveller}
              },
              {
                url: url,
                body: this.rollbackPreviousBody
              }
            );

            this.rollbackSetPreviousBody(this.traveller);

            return store.dispatch("fetchMe");
          })
          .catch(e => {
            this.editErrorAlert = true;
            console.log(e);
          });
      }
    },
    /**
     * Undoes the last action and calls getTraveller() afterwards
     */
    undo: async function() {
      try {
        const actions = [this.resetAlerts, this.getTraveller];
        await this.rollbackUndo(actions); 
      } catch (err) {
        console.log(err);
        this.undoRedoErrorAlert = true;
      }
    },

    /**
     * Redoes the last action and calls getTraveller() afterwards
     */
    redo: async function() {
      try {      
        const actions = [this.resetAlerts, this.getTraveller];
        await this.rollbackRedo(actions);
      } catch (err) {
        console.log(err);
        this.undoRedoErrorAlert = true;
      }

    },
  },

  /**
   * Gets the id of the user upon component creation.
   */
  created: function() {
    this.id = this.$route.params.id;
    if (!this.id) {
      this.id = store.getters.getUser.id;
    }
    this.getTraveller();
  }
};
</script>
