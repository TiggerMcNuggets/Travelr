<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-container fluid v-if="selectedTrip">
    <PageHeader
      :title="selectedTrip.trip.name"
      :undo="undo"
      :redo="redo"
      :canRedo="rollbackCanRedo"
      :canUndo="rollbackCanUndo"
      :options="headerOptions"
      enableBackButton
    />

    <AddGroup 
      :closeGroupDialog="closeGroupDialog" 
      :dialogActive="addUsergroupDialogActive"
    />

    <v-layout row wrap class="content">
      <TripEditor 
        :updateTrip="updateTrip"
        :hasAdjacentIdentical="hasAdjacentIdentical"
        :canEdit="canEdit"
      />

      <TripDetails
        :trip="selectedTrip"
        :hasWritePermissions="hasWritePermissions"
        :updateTrip="updateTrip"
        :pushStack="pushStack"
        :canEdit="canEdit"
      />

      <TripMap 
        v-if="isLarge || isExtraLarge"
        :nodes="selectedTrip.trip.nodes"
      />
    </v-layout>
    <v-dialog v-model="showUploadSection" width="800">
      <MediaUpload
        :uploadMedia="uploadMedia"
        :openUploadDialog="toggleShowUploadPhoto"
        :closeUploadDialog="toggleShowUploadPhoto"
        :hasNoAlbums="true"
      ></MediaUpload>
    </v-dialog>
  </v-container>
</template>

<style>
.temp-map {
  height: 100%;
  width: 100%;
  background-color: lightblue;
}

.content {
  min-height: calc(100vh - 100px);
}
</style>

<script>
import DeviceSizeMixin from "../mixins/DeviceSizeMixin.vue";
import RollbackMixin from "../mixins/RollbackMixin";
import StoreTripsMixin from "../mixins/StoreTripsMixin";
import PageHeader from "../common/header/PageHeader";
import TripDetails from "./TripDetails";
import TripMap from "./TripMap";
import AddGroup from "./tripgroups/AddGroup";
import TripEditor from "./viewtrip/TripEditor";
import MediaUpload from "../media/MediaUpload";
import { store } from "../../store/index";
import {
  tripAssembler,
  noAdjacentIdenticalDestinations
} from "./trips_destinations_util";
import { RepositoryFactory } from "../../repository/RepositoryFactory";

let mediaRepository = RepositoryFactory.get("media");

export default {
  store,
  components: {
    PageHeader,
    TripDetails,
    AddGroup,
    TripEditor,
    TripMap,
    MediaUpload
  },

  mixins: [RollbackMixin, StoreTripsMixin, DeviceSizeMixin],

  // local variables
  data() {
    return {
      addUsergroupDialogActive: false,
      isMyProfile: false,
      isAdmin: store.getters.getIsUserAdmin,
      userId: this.$route.params.id,
      hasAdjacentIdentical: false,
      previousTripId: Number(this.$route.params.trip_id),
      showUploadSection: false
    };
  },

  computed: {
    /**
     * Checks if user is on a device large enough to edit trips easy
     * @return true or false
     */
    canEdit() {
      if (!this.isExtraSmall) {
        return true;
      } else {
        return false;
      }
    },

    /**
     * Checks if the user is permitted to write to the trip
     * @return true or false
     */
    hasWritePermissions() {
      return this.isTripOwner || this.isGroupOwner || this.isAdmin
    },

    /**
     * Checks if the user is the trip owner
     * @return true or false: whether the user is the trip owner
     */
    isTripOwner() {
      return this.selectedTrip.root.user.id === this.$store.getters.getUser.id;
    },

    /**
     * Checks if the user is the group owner
     * @return true or false: whether the user is the group owner
     */
    isGroupOwner() {
      let isOwn = false;
      if (!this.selectedTrip) return isOwn;

      this.selectedTrip.trip.usergroup.forEach(user => {
        if (user.userId === this.$store.getters.getUser.id && user.owner) {
          isOwn = true;
        }
      });
      return isOwn;
    },

    /**
     * Gets the header optoins for the view trip page.
     * @return The list of header options
     */
    headerOptions() {
      return this.selectedTrip &&
        this.selectedTrip.trip.id == this.selectedTrip.root.id &&
        (this.isTripOwner || this.isGroupOwner || this.isAdmin)
        ? [
            {
              action: () => {
                this.openGroupDialog();
              },
              icon: "people_alt",
              title: "Manage Group"
            },
            {
              action: () => {
                this.toggleShowUploadPhoto();
              },
              icon: "add_photo_alternate",
              title: "Add Photos"
            }
          ]
        : [
            {
              action: () => {
                this.toggleShowUploadPhoto();
              },
              icon: "add_photo_alternate",
              title: "Add Photos"
            }
          ];
    }
  },
  methods: {
    /**
     * Sends a request to the backend containing formdata with the image to be added to a specified album
     * given an user id and an album id.
     */
    uploadToAlbum(albumId, file) {
      let formData = new FormData();
      formData.append("picture", file);

      mediaRepository
        .uploadMediaToAlbum(this.userId, albumId, formData)
        .then(() => {
          this._getTrip(this.userId, this.tripId).then(() => {
            this.rollbackSetPreviousBody(tripAssembler(this.selectedTrip));
          });
        });
    },

    /**
     * Toggles whether or not to display the photo upload section
     */
    toggleShowUploadPhoto() {
      this.showUploadSection = !this.showUploadSection;
    },

    /**
     * Uploads the given media files to the backend.
     */
    uploadMedia(files) {
      let albumId = this.selectedTrip.root.albumId;
      for (let i = 0; i < files.length; i++) {
        let file = files[i];
        this.uploadToAlbum(albumId, file);
      }

      this.toggleShowUploadPhoto();
    },

    /**
     * Opens the group dialog
     */
    openGroupDialog() {
      this.addUsergroupDialogActive = true;
    },

    /**
     * Closes the group dialog
     */
    closeGroupDialog() {
      this.addUsergroupDialogActive = false;
    },

    /**
     * Updates and refetches trip
     * @param userId the user's id
     * @param tripId the trip's id
     * @param trip the trip body
     */
    async updateTripAndPopulate(userId, tripId, trip) {
      await this._putTrip(userId, tripId, trip);
      return this.updateViewTripPage();
    },

    /**
     * Updates and refetches trip and pushes to undo redo stack
     */
    async updateTrip() {
      // Get request parameters and body
      const userId = this.userId;
      const tripId = this.tripId;
      let trip = tripAssembler(this.selectedTrip);

      // Get undo request parameters and body
      const previousTripId = this.previousTripId;
      const rollbackPreviousBody = this.rollbackPreviousBody;

      // Validate
      if (noAdjacentIdenticalDestinations(this.selectedTrip)) {
        this.hasAdjacentIdentical = false;
        try {
          await this.updateTripAndPopulate(userId, tripId, trip);

          // Add to undo redo stack
          let checkpoint = {
            action: async () =>
              await this.updateTripAndPopulate(userId, tripId, trip),
            reaction: async () =>
              await this.updateTripAndPopulate(
                userId,
                previousTripId,
                rollbackPreviousBody
              )
          };
          this.pushStack(checkpoint);
          this.rollbackSetPreviousBody(trip);
          this.previousTripId = tripId;
        } catch (e) {
          console.log(e);
        }
      } else {
        this.hasAdjacentIdentical = true;
      }
    },

    /**
     * Refreshes trip view after update
     */
    updateViewTripPage() {
      let trip = this.selectedTrip;

      // Sorts the destinations ensure they are in the order of their ordinal
      let orderedDests = this.selectedTrip.trip.nodes.sort(function(a, b) {
        return a.ordinal - b.ordinal;
      });

      trip.destinations = orderedDests;
      this._setSelectedTrip(trip);
    },

    /**
     * Undoes the last action and calls setDestination() afterwards
     */
    undo: function() {
      const actions = [];
      this.rollbackUndo(actions);
    },

    /**
     * Redoes the last action and calls setDestination() afterwards
     */
    redo: function() {
      const actions = [];
      this.rollbackRedo(actions);
    }
  },

  watch: {
    selectedTrip: function() {
      this.updateViewTripPage();
    }
  },

  /**
   * Runs when component is created
   */
  created: function() {
    this.isMyProfile = store.getters.getUser.id == this.$route.params.id;
    // If the person viewing the trip is not admin and does not own the trip then takes them back to the page they were on
    if (!this.isMyProfile && !this.isAdmin) {
      this.$router.go(-1);
    }
    this._getTrip(this.userId, this.tripId).then(() => {
      this.rollbackSetPreviousBody(tripAssembler(this.selectedTrip));
    });
  }
};
</script>
