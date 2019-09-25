<template>
  <div>
    <h2>General</h2>
    <!-- Download iCal button -->
    <v-btn icon @click="downloadICal()" flat color="error">
      <v-icon>calendar_today</v-icon>
    </v-btn>

    <!-- Download trip pdf -->
    <v-btn icon @click="downloadTripPdf()" flat color="error">
      <v-icon>picture_as_pdf</v-icon>
    </v-btn>

    <!-- Email iCal and PDF button -->
    <v-btn v-if="hasWritePermissions" @click="emailTrip()" flat fab small color="error" >
      <v-icon>email</v-icon>
    </v-btn>

    <!-- Upload Files Button -->
    <v-btn icon @click="toggleShowFileUpload()" flat small color="error" >
      <v-icon>attach_file</v-icon>
    </v-btn>

    <v-dialog v-model="showUploadSection" width="800">
      <FileUpload
        :openUploadDialog="toggleShowFileUpload"
        :closeUploadDialog="toggleShowFileUpload"
        :userId="userId"
        :tripId="tripId"
      ></FileUpload>
    </v-dialog>


    <UserFiles 
      v-if="trip.trip.usergroup.length" 
      :hasWritePermissions="hasWritePermissions"
      :pushStack="pushStack"
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

  export default {
    name: "TripFiles",

    components: {
      FileUpload,
    },

    props: {
      trip: Object,
      hasWritePermissions: Boolean,
      pushStack: Function
    },

    components: {
      UserFiles
    },

    data() {
      return {
        userId: this.$route.params.id,
        tripId: this.$route.params.trip_id,
        isAdmin: store.getters.getIsUserAdmin,
        showUploadSection: false
      };
    },

    methods: {
      /**
       * Emails the iCal and pdf to all of the users in the group
       */
      emailTrip() {
        tripRepository.emailPdfAndICal(this.userId, this.tripId);
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
      toggleShowFileUpload() {
        this.showUploadSection = !this.showUploadSection;
      },

    },
  };
</script>
