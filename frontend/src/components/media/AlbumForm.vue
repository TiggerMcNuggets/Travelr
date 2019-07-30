<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">{{ title }}</h2>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text>
      <v-layout row wrap>
        <v-flex xs12 ml-3 mr-3>
          <v-text-field label="Title" v-model="name" maxlength="50"></v-text-field>
        </v-flex>
      </v-layout>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="closeAlbumDialog()">Cancel</v-btn>
      <v-btn ma-3 color="primary" flat @click="clearNameAndSubmit">{{ title }}</v-btn>
    </v-card-actions>
  </v-card>
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
