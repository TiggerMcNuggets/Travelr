<template>
  <v-container class="section-container">
    <v-flex class="section-body">
      <v-text-field
        label="New Group Name"
        placeholder="Your group name"
        outlined
        v-model="name"
        maxlength="50"
      ></v-text-field>
      <v-textarea
        label="New Group Description"
        placeholder="Your group description"
        outlined
        v-model="description"
        maxlength="50"
      ></v-textarea>
      <v-flex pb-2 pt-2>
        <v-btn color="error" @click="clearAndSubmit">Create New Group</v-btn>
      </v-flex>
    </v-flex>
    <v-alert type="success" v-model="successful">User group successfully created.</v-alert>
    <v-alert type="error" v-model="failure">{{failureMessage}}</v-alert>
  </v-container>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let userGroupRepository = RepositoryFactory.get("userGroup");

export default {
  props: {
    title: String,
    album: Object,
    updateUserGroups: Function
  },

  data() {
    return {
      name: "",
      description: "",
      successful: false,
      failure: false,
      failureMessage: ""
    };
  },

  methods: {
    /**
     * Either edits or creates the album (depending on what submitAlbum is set to)
     * and then clears the text field
     */
    clearAndSubmit() {
      this.createGroup();
      this.clearFields();
    },

    /**
     * Creates a new user group for the user.
     */
    createGroup() {
      this.successful = false;
      this.failure = false;
      userGroupRepository
        .createUserGroup(this.$store.getters.getUser.id, {
          name: this.name,
          description: this.description
        })
        .then(() => {
          this.successful = true;
          this.updateUserGroups();
        })
        .catch(error => {
          this.failure = true;
          this.failureMessage = error.response.data;
        });
    },

    /**
     * Clears the fields.
     */
    clearFields() {
      this.name = "";
      this.description = "";
    }
  }
};
</script>
