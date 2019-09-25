<template>
  <div>
    <v-img src="http://images.clipartpanda.com/file-clipart-xigKMeMjT.png" height="100px" :contain="true">
    </v-img>

    <v-layout pa-1 column align-center>
      <span class="file-name">{{file.name}}</span>
      <v-card-actions>

        <v-layout >
          <v-flex>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn v-on="on" @click="getFile(file)" color="primary" class="download-button" flat fab small>
                  <v-icon dark>cloud_download</v-icon>
                </v-btn>
              </template>
              <span>Download file</span>
            </v-tooltip>
          </v-flex>

          <v-flex>
            <v-tooltip bottom v-if="hasWritePermissions">
              <template v-slot:activator="{ on }">
                <v-btn v-on="on" @click="deleteFile(file)" color="error" class="delete-button" flat fab small>
                  <v-icon dark>delete</v-icon>
                </v-btn>
              </template>
              <span>Delete file</span>
            </v-tooltip>
          </v-flex>
        </v-layout>

      </v-card-actions>
    </v-layout>
    <v-divider></v-divider>
  </div>
</template>

<script>
export default {
  name: "UserFile",

  props: {
    file: Object,
    getFile: Function,
    deleteFile: Function,
    hasWritePermissions: Boolean,
  },

  components: {},

  data() {
    return {};
  },

  methods: {},
};
</script>
