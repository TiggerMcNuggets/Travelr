<template>
  <v-card class="file-card">
    <v-img
      src="http://images.clipartpanda.com/file-clipart-xigKMeMjT.png" height="100" :contain="true"
    >
    </v-img>
            <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-btn
              v-on="on"
              @click="getFile(file)"
              color="error"
              class="download-button"
              flat
              fab
            >
              <v-icon dark>cloud_download</v-icon>
            </v-btn>
          </template>
          <span>Download file</span>
        </v-tooltip>

        <v-tooltip bottom v-if="hasWritePermissions">
          <template v-slot:activator="{ on }">
            <v-btn
              v-on="on"
              color="error"
              class="delete-button"
              flat
              fab
            >
              <v-icon dark>delete</v-icon>
            </v-btn>
          </template>
          <span>Delete file</span>
        </v-tooltip>
    <v-card-actions>
      <span class="file-name">{{file.name}}</span>
    </v-card-actions>
  </v-card>
</template>

<style>
  .file-card {
    padding-top: 10px;
  }

  .file-name {
    text-overflow: ellipsis !important;
    white-space: nowrap;
    overflow: hidden;
  }
</style>

<script>
  export default {
    name: "UserFile",

    props: {
      file: Object,
      getFile: Function,
      hasWritePermissions: Boolean
    },

    components: {},

    data() {
      return {};
    },

    methods: {},
  };
</script>
