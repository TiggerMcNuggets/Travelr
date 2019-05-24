<template>
  <v-card>
    <div class="outer-container">
      <div class="container">
        <div class="section">
          <div class="dest-name">
            <v-btn
              class="upload-toggle-button"
              fab
              small
              dark
              color="indigo"
              @click="$router.go(-1)"
            >
              <v-icon dark>keyboard_arrow_left</v-icon>
            </v-btn>
            <h2 class="headline">Edit Destination</h2>
            <undo-redo-buttons
              :canRedo="rollbackCanRedo()"
              :canUndo="rollbackCanUndo()"
              :undo="undo"
              :redo="redo"></undo-redo-buttons>
          </div>
        </div>

        <v-divider class="photo-header-divider"></v-divider>
        <destination-edit-fields
                :updateDestinationCallback="updateDestination"
                :prefillData="{...prefillData}">
        </destination-edit-fields>

      </div>
    </div>
  </v-card>
</template>

<style>
.outer-container {
  text-align: center;
}

.buttons-div {
  margin-top: 2em;
}

.container {
  align-self: center;
  display: inline-block;
  text-align: left;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepository = RepositoryFactory.get("destination");
import { rules } from "../form_rules";
import RollbackMixin from "../mixins/RollbackMixin.vue";
import UndoRedoButtons from "../common/rollback/UndoRedoButtons.vue";
import DestinationEditFields from "./DestinationEditFields"

export default {
  mixins: [RollbackMixin],
  components: {
    UndoRedoButtons: UndoRedoButtons,
    DestinationEditFields: DestinationEditFields
  },
  data() {
    return {
      prefillData: null,
      isError: false,
      ...rules
    };
  },
  methods: {

    /**
     * Requests a destination and sets the prefillData property to it
     */
    setDestination: function() {
      destinationRepository
        .getDestination(this.$route.params.id, this.$route.params.dest_id)
        .then(result => {
          this.prefillData = result.data;

          // This is set to later be pushed as a reaction to the rollback stack
          this.rollbackSetPreviousBody(result.data);
        });
    },

    /**
     * Updates a destination by through a request to the API based on the updated data in the form.
     * This function will first check if the data is valid and only submit successfully if it is.
     * A checkpoint is pushed to the undo redo stack containing information for the action and reaction
     */
    updateDestination: function(destination) {
      const userId = this.$route.params.id;
      const destId = this.$route.params.dest_id;

      // Call the update request
      destinationRepository
        .updateDestination(
          userId,
          destId,
          destination
        )
        .then(() => {
          const url = `/users/${userId}/destinations/${destId}`;

          // Pushes checkpoint containing type of action, action body, and reaction body
          this.rollbackCheckpoint(
            'PUT',
            {
              url: url,
              body: {...destination}
            },
            {
              url: url,
              body: this.rollbackPreviousBody
            }
          );

          // Update previous body to be used for the next checkpoints reaction
          this.rollbackSetPreviousBody(destination);
          this.doneUpdatingCallback();

          this.isError = false;
        })
        .catch((e) => {
          console.error(e);
          this.isError = true;
        });
    },

    /**
     * Undoes the last action and calls setDestination() afterwards
     */
    undo: function() {
      const actions = [this.setDestination];
      this.rollbackUndo(actions); 
    },

    /**
     * Redoes the last action and calls setDestination() afterwards
     */
    redo: function() {
      const actions = [this.setDestination];
      this.rollbackRedo(actions);
    },

    /**
     * Redirects the user back to the previous page.
     */
    routeBackToPrevPage: function() {
      this.$router.go(-1); // sends you back to the previous page
    }
  },

  /**
   * Gets the current destination data to display in the form to update on component creation.
   */
  created: function() {
    this.setDestination();
  }
};
</script>
