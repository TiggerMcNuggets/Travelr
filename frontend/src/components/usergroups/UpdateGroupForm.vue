<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Update User Group Details</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <v-text-field
        label="New Group Name"
        placeholder="Your group name"
        outlined
        v-model="userGroup.name"
        maxlength="50"
      ></v-text-field>
      <v-textarea
        label="New Group Description"
        placeholder="Your group description"
        outlined
        v-model="userGroup.description"
        maxlength="50"
      ></v-textarea>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat @click="closeDialog()">Cancel</v-btn>
      <v-btn ma-3 color="primary" flat @click="updateGroup()">Update Group</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let userGroupRepository = RepositoryFactory.get("userGroup");
import {deepCopy} from "../../tools/deepCopy";
export default {
  props: {
    name: String,
    description: String,
    userGroupId: Number,
    closeDialog: Function,
    rollbackCheckpoint: Function
  },

  data() {
    return {
      rollbackBody: {name: "", description: ""},
      userGroup: {
        name: "",
        description: ""
      }
    };
  },

  methods: {
    /**
     * Updates a user group for the user.
     */
    updateGroup() {
      userGroupRepository
        .updateUserGroup(
          this.$store.getters.getUser.id,
          this.userGroupId,
          this.userGroup
        )
        .then(() => {
          // Pushes checkpoint containing type of action, action body, and reaction body
          const url = `/users/${this.$store.getters.getUser.id}/group/${this.userGroupId}`
          this.rollbackCheckpoint(
                  'PUT',
                  {
                    url: url,
                    body: this.userGroup
                  },
                  {
                    url: url,
                    body: this.rollbackBody
                  }
          );
          this.rollbackBody = this.userGroup;
          this.closeDialog();
        });
    }
  },

  /**
   * Sets the user group name and description from the props on mount
   */
  mounted() {
    this.userGroup.name = deepCopy(this.name);
    this.userGroup.description = deepCopy(this.description);
    this.rollbackBody = deepCopy(this.userGroup);
  }
};
</script>
