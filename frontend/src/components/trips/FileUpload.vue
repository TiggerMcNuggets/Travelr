<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Upload File</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <div id="dropzone">Drag files or click to open file explorer.</div>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="closeUploadDialog()">Cancel</v-btn>
      <v-btn
        ma-3
        color="primary"
        flat
      >Upload Files</v-btn>
    </v-card-actions>
  </v-card>
</template>


<style>

#dropzone {
  background: white;
  border-radius: 5px;
  border: 2px dashed rgb(0, 135, 247);
  border-image: none;
  padding: 50px;
}

#dropzone.dragover {
  background: rgba(0, 135, 247, 0.4);
}
</style>


<script>

export default {

  props: {
    openUploadDialog: Function,
    closeUploadDialog: Function,
  },

  // local variables
  data() {
    return {
      files: [],
      rawFiles: []
    };
  },

  methods: {
  }
};
</script>
