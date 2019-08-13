<template>
  <v-container class="section-container">
    <v-layout row wrap class="section-body">
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
      <v-flex pb-3 pt-2>
        <v-btn color="error" @click="clearAndSubmit">Create New Group</v-btn>
      </v-flex>
    </v-layout>
    <v-divider></v-divider>
  </v-container>
</template>

<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let usergroupRepository = RepositoryFactory.get("userGroup");

export default {
  props: {
    title: String,
    album: Object
  },

  data() {
    return {
      name: "",
      description: ""
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
      usergroupRepository.createUserGroup(this.$store.getters.getUser.id, {"name": this.name, "description": this.description}).then(response => console.log(response))
    },

    /**
     * Clears the fields.
     */
    clearFields() {
      this.name = ""
      this.description = ""
    }
  }
};
</script>
