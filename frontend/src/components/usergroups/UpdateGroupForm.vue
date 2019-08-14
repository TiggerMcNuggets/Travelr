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
        v-model="usergroup.name"
        maxlength="50"
      ></v-text-field>
      <v-textarea
        label="New Group Description"
        placeholder="Your group description"
        outlined
        v-model="usergroup.description"
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
let usergroupRepository = RepositoryFactory.get("userGroup");

export default {
  props: {
    name: String,
    description: String,
    usergroupId: Number,
    closeDialog: Function
  },

  data() {
    return {
      usergroup: {
        name: "",
        description: ""
      }
    };
  },

  methods: {
    /**
     * Creates a new user group for the user.
     */
    updateGroup() {
      usergroupRepository
        .updateUserGroup(
          this.$store.getters.getUser.id,
          this.usergroupId,
          this.usergroup
        )
        .then(response => {
          this.closeDialog();
        });
    }
  },

  /**
   * Sets the user group name and description from the props on mount
   */
  mounted() {
    this.usergroup.name = this.name;
    this.usergroup.description = this.description;
  }
};
</script>
