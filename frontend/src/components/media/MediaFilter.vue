<template>

  <v-flex xs2>
    <v-list class="list">
      <v-list-tile v-for="(item, i) in filterCategories" :key="i" @click="changeFilter(i)" class="tile">
        <v-list-tile-content>
          <v-list-tile-title v-html="item.title +  ` (` + item.count +`)`"></v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>

    <v-btn class="upload-button" @click="toggleUploadDialogue" flat>
        <v-icon left dark>add_photo_alternate</v-icon>
        Upload Media
    </v-btn>


    <v-btn class="create-album-button" @click="toggleCreateAlbumDialogue" flat>
        <v-icon left dark>add_to_photos</v-icon>
        Create Album
    </v-btn>

    <v-dialog v-model="uploadDialogActive" width="800">
      <MediaUpload :uploadImages="uploadMedia" :toggleUploadDialogue="toggleUploadDialogue"/>
    </v-dialog>

    <v-dialog v-model="createAlbumDialogActive" width="800">
      <AlbumCreate :toggleCreateAlbumDialogue="toggleCreateAlbumDialogue"/>
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
    margin-top: 100px;
    margin-left: 0px;
    height: 48px;
  }

  .create-album-button {
      margin-top: 0px;
      margin-left: 0px;
      height: 48px;
  }
</style>

<script>
  import MediaUpload from "../photos/PhotoUpload";
  import AlbumCreate from "./AlbumCreate";

  export default {
    name: "MediaFilter",

    props: {
      changeFilter: Function,
      mediaCounts: Object
    },

    components: {
      MediaUpload,
      AlbumCreate
    },

    data() {
      return {
        uploadDialogActive: false,
        createAlbumDialogActive: false
      }
    },

    computed: {
      filterCategories: function () {
        return [
          {
            title: 'All', count: this.mediaCounts.all
          },
          {
            title: 'Albums', count: this.mediaCounts.albums
          },
          {
            title: 'Photos', count: this.mediaCounts.images
          },
          {
            title: 'Videos', count: this.mediaCounts.videos
          }
        ];
      }
    },

    methods: {
      uploadMedia() {
        this.toggleUploadDialogue();
      },

      toggleUploadDialogue() {
        this.uploadDialogActive = !this.uploadDialogActive;
      },

      toggleCreateAlbumDialogue() {
        this.createAlbumDialogActive = !this.createAlbumDialogActive;
      }
    }
  }
</script>