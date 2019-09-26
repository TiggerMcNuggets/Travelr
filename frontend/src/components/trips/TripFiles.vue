<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <h2>General</h2>
    <!-- Download iCal button -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" icon @click="downloadICal()" flat fab color="error">
          <v-icon>calendar_today</v-icon>
        </v-btn>
      </template>
      <span>Export Calendar</span>
    </v-tooltip>

    <!-- Download trip pdf -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" icon @click="downloadTripPdf()" flat fab color="error">
          <v-icon>picture_as_pdf</v-icon>
        </v-btn>
      </template>
      <span>Download Trip PDF</span>
    </v-tooltip>


    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" v-if="hasWritePermissions" @click="emailTrip()" flat fab color="error" >
          <v-icon>contact_mail</v-icon>
        </v-btn>
      </template>
      <span>Email Trip & Calendar To Group</span>
    </v-tooltip>

    <!-- Email iCal and PDF button -->
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" v-if="hasWritePermissions" @click="emailTrip(true)" flat fab color="error" >
          <v-icon>email</v-icon>
        </v-btn>
      </template>
      <span>Email Trip & Calendar To You</span>
    </v-tooltip>

    <!-- Upload Files Button -->
    <v-btn icon @click="openFileUpload()" flat small color="error" >
      <v-icon>attach_file</v-icon>
    </v-btn>

    <v-dialog v-model="showUploadSection" width="800">
      <FileUpload
        :closeUploadDialog="closeFileUpload"
        :userId="userId"
        :tripId="tripId"
        :getFiles="getFiles"
      ></FileUpload>
    </v-dialog>


    <UserFiles
      :hasWritePermissions="hasWritePermissions"
      :pushStack="pushStack"
      :getFiles="getFiles"
      :files="files"
    />
  </div>
</template>

<style>
  .download-calendar {
    display: inline-block;
  }
</style>

<script>
  import { store } from "../../store/index";
  import { RepositoryFactory } from "../../repository/RepositoryFactory";
  import FileUpload from "./FileUpload";
  import { download } from "./trips_destinations_util";
  import UserFiles from "./viewtrip/UserFiles";

  let tripRepository = RepositoryFactory.get("trip");
  let fileRepository = RepositoryFactory.get("file");

  export default {
    name: "TripFiles",

    components: {
      FileUpload,
      UserFiles
    },

    props: {
      trip: Object,
      hasWritePermissions: Boolean,
      pushStack: Function
    },

    data() {
      return {
        userId: this.$route.params.id,
        tripId: this.$route.params.trip_id,
        isAdmin: store.getters.getIsUserAdmin,
        showUploadSection: false,
        files: []
      };
    },

    methods: {
      /**
       * Fetches files for a trip
       */
      async getFiles() {
        const res = await fileRepository.getFilesForTrip(this.userId, this.tripId);
        this.files = res.data;
        //closes upload box after updating main trip page
        this.closeFileUpload();
      },

      /**
       * Emails the iCal and pdf to all of the users in the group
       */
      emailTrip(onlyMe) {
        tripRepository.emailPdfAndICal(this.userId, this.tripId, onlyMe)
          .then(() => {
            this.showSuccessSnackbar(
              onlyMe ? this._snackbarMessages.emailMePdfAndIcalSuccess : this._snackbarMessages.emailGroupPdfAndIcalSuccess,
              4500
            );
          })
          .catch(() => {
            this.showErrorSnackbar(this._snackbarMessages.failedToSendEmail, 4500);
          })
        ;
      },

      /**
       * Downloads the trip from the database as an ics
       */
      downloadICal() {
        tripRepository.downloadICal(this.userId, this.tripId).then(res => {
          download(res, `${this.trip.trip.name}.ics`);
        });
      },

      /**
       * Downloads the trip from the database as a pdf
       */
      downloadTripPdf() {
        tripRepository.downloadTripPdf(this.userId, this.tripId).then(res => {
          download(res, `${this.trip.trip.name}.pdf`);
        })
      },

      /**
       * controls when file upload ox can be seen
       */
      openFileUpload() {
        this.showUploadSection = true;
      },

      /**
       * controls when file upload ox can be seen
       */
      closeFileUpload() {
        this.showUploadSection = false;
      },

    },
  };
</script>
