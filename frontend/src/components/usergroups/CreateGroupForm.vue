<template>
  <v-container class="section-container">
    <!-- <v-layout class="section-heading" d-flex justify-space-between> -->
    <!-- <h3>Create New Group</h3> -->
    <!-- <v-flex class="section-options-container" align-items-end>
        <v-btn
          icon
          @click="() => {searchActive = false; createGroupActive=!createGroupActive}"
          flat
          small
          color="grey"
          v-on="on"
        >
          <v-icon>add</v-icon>
        </v-btn>
        <v-btn
          icon
          @click="() => {searchActive = !searchActive; createGroupActive=false}"
          flat
          small
          color="grey"
          v-on="on"
        >
          <v-icon>search</v-icon>
        </v-btn>
      </v-flex>
    </v-layout>-->

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
        v-model="name"
        maxlength="50"
      ></v-textarea>
      <v-flex pb-3 pt-2>
        <v-btn color="error">Create New Group</v-btn>
      </v-flex>
    </v-layout>
    <v-divider></v-divider>
  </v-container>
</template>

<script>
export default {
  props: {
    title: String,
    closeAlbumDialog: Function,
    submitAlbum: Function,
    album: Object
  },

  data() {
    return {
      name: ""
    };
  },

  mounted() {
    this.name = this.album ? this.album.name : "";
  },

  watch: {
    album: function(newAlbum, oldAlbum) {
      if (newAlbum !== oldAlbum) this.name = this.album ? this.album.name : "";
    }
  },

  methods: {
    /**
     * Either edits or creates the album (depending on what submitAlbum is set to)
     * and then clears the text field
     */
    clearNameAndSubmit() {
      this.submitAlbum(this.name);
      this.closeAlbumDialog();
    }
  }
};
</script>
