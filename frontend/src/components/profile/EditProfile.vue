<template>
  <v-container fluid>
    <PageHeader
      title="Edit Profile"
      :undo="undo"
      :redo="redo"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      enableBackButton
      :options="[]"
    />
    <v-form ref="form" v-model="isValid" lazy-validation>
      <v-flex text-xs-left>
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
        </div>
        <v-btn :disabled="!isValid" large color="error" @click="handleEdit">Save</v-btn>
        <v-btn @click="submitFile" large outline color="error">Upload Photo</v-btn>
        <v-alert :value="editErrorAlert" type="error">Cannot edit profile</v-alert>
        <v-alert :value="undoRedoErrorAlert" type="error">Cannot undo or redo</v-alert>
      </v-flex>
    </v-form>
  </v-container>
</template>

<script>
import userRepo from "../../repository/UserRepository";
import TravellerForm from "../common/travellerForm/TravellerForm";
import travellerFormHelper from "../common/travellerForm/travellerFormHelper";
import dateTime from "../common/dateTime/dateTime.js";
import { uploadProfilePic } from "../../repository/PersonalPhotosRepository";
import RollbackMixin from "../mixins/RollbackMixin.vue";

import { store } from "../../store/index";
import PageHeader from "../common/header/PageHeader";

export default {
  name: "EditProfile",
  components: { PageHeader, TravellerForm },

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
      undoRedoErrorAlert: false
    };
  },
  methods: {
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
        store.dispatch("fetchMe")
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
        let previousBody = this.setTravellerToFields({ ...result.data });
        previousBody = this.setFieldsToTraveller(previousBody);
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
      return traveller;
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
            const url = `/travellers/${this.id}`;

            this.rollbackCheckpoint(
              "PUT",
              {
                url: url,
                body: { ...this.traveller }
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
    }
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
