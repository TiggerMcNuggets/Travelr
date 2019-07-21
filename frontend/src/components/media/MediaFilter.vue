<template>
  <v-flex xs2>
    <v-list class="list">
      <v-list-tile v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(i)" class="tile">
        <v-list-tile-content>
          <v-list-tile-title v-html="item.title +  ` (` + item.count +`)`" ></v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>

    <v-btn class="upload-button" @click="() => uploadDialogActive = !uploadDialogActive" flat>
      <v-icon left dark>add</v-icon>
      Upload
    </v-btn>

    <v-dialog v-model="uploadDialogActive" width="800">
      <MediaUpload :uploadImages="uploadMedia" />
    </v-dialog>
  </v-flex>
</template>

<style scoped>
  .list {
    background: none;
  }

  .tile:hover {
    background: none;
  }

  .tile:active {
    color: deeppink;
  }

  .upload-button {
    margin-top: 30px;
    margin-left: 0px;
    height: 48px;
  }
</style>

<script>
import MediaUpload from "../photos/PhotoUpload";

export default {
  name: "MediaFilter",

  props: {
    updateMediaFilter: Function,
    mediaCounts: Array
  },

  components: {
    MediaUpload
  },

 
  data() {
    return {
      uploadDialogActive: false,
      filterCategories: [
        {
          title: 'All', count: 0
        },
        {
          title: 'Albums', count: 0
        },
        {
          title: 'Photos', count: 0
        },
        {
          title: 'Videos', count: 0
        }
      ]
    }
  },

   update() {
    console.log(this.uploadDialogActive);
  },


  computed: {
  },

  methods: {
    uploadMedia() {
      this.uploadDialogActive = false;
    }
  }
}
</script>
