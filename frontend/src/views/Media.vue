<template>
  <v-container fluid>
    <PageHeader
      v-if="viewingAlbum"
      :title="activeAlbumMetadata.name"
      disableUndoRedo
      enableBackButton
      :backButtonOverride="navigateBack"
      :options="albumOptions"
    />
    <PageHeader v-else title="Media" disableUndoRedo :options="normalOptions"/>

    <v-layout row wrap>
      <v-flex xs2 v-if="viewingAlbum">
        <MediaFilter :changeFilter="changeFilter" :mediaCounts="mediaCounts"></MediaFilter>
      </v-flex>
      <v-flex xs10 v-if="viewingAlbum">
        <MediaGrid
          :filteredMedia="filteredMedia"
          :openElement="openElement"
          :getAllAlbums="getAllAlbums"
          :openEditAlbumDialog="openEditAlbumDialog"
        ></MediaGrid>
      </v-flex>
      <v-flex xs12 v-else>
        <MediaGrid
          :filteredMedia="filteredMedia"
          :openElement="openElement"
          :getAllAlbums="getAllAlbums"
          :openEditAlbumDialog="openEditAlbumDialog"
        ></MediaGrid>
      </v-flex>
    </v-layout>

    <v-dialog v-model="uploadDialogActive" width="800">
      <MediaUpload
        :uploadMedia="uploadMedia"
        :openUploadDialog="openUploadDialog"
        :closeUploadDialog="closeUploadDialog"
        :allAlbums="this.organisedMedia.albums"
        :createNewAlbum="createNewAlbum"
        :openCreateAlbumDialog="openCreateAlbumDialog"
        :closeCreateAlbumDialog="closeCreateAlbumDialog"
        :activeAlbumMetadata="activeAlbumMetadata"
      ></MediaUpload>
    </v-dialog>

    <v-dialog v-if="!viewingAlbum" v-model="createAlbumDialogActive" width="500">
      <AlbumCreate
        :createNewAlbum="createNewAlbum"
        :closeCreateAlbumDialog="closeCreateAlbumDialog"
      />
    </v-dialog>

    <v-dialog v-if="!viewingAlbum" v-model="editAlbumDialogActive" width="500">
      <AlbumEdit
        :editAlbum="editNewAlbum"
        :closeEditAlbumDialog="closeEditAlbumDialog"
        :album="editAlbumObject"
      />
    </v-dialog>

    <v-dialog v-model="viewMediaDialogActive" :width="clickedImageWidth">
      <MediaDialog
        :clickedImage="clickedImage ? clickedImage : {}"
        :closeMediaDialog="() => viewMediaDialogActive = false"
        :updateMedia="updateMedia"
        :deleteMedia="deleteMedia"
        :getAllAlbums="getAllAlbums"
        :openConfirmDelete="() => {confirmDeletionDialogActive = true}"
        :isMyProfile="isMyProfile"
        :isAdminUser="isAdminUser"
      />
    </v-dialog>

    <v-dialog v-model="confirmDeletionDialogActive" width="500">
      <ConfirmDelete
        title="Confirm Delete"
        text="Would you like to delete this media from all albums?"
        :confirm="() => {deleteAndCloseDialog(true)}"
        :cancel="() => {deleteAndCloseDialog(false)}"
      />
    </v-dialog>
  </v-container>
</template>

<script>
import MediaFilter from "../components/media/MediaFilter";
import MediaGrid from "../components/media/MediaGrid";
import PageHeader from "../components/common/header/PageHeader";
import MediaUpload from "../components/media/MediaUpload";
import AlbumCreate from "../components/media/AlbumCreate";
import AlbumEdit from "../components/media/AlbumEdit";
import MediaDialog from "../components/media/MediaDialog";
import ConfirmDelete from "../components/common/ConfirmDialog";

import base_url from "../repository/BaseUrl";
import { deepCopy } from "../tools/deepCopy";
import { RepositoryFactory } from "../repository/RepositoryFactory";

let mediaRepository = RepositoryFactory.get("media");

export default {
  name: "Media",

  data() {
    return {
      activeFilter: "Albums",
      allMedia: [],
      mediaMap: {},
      activeMedia: [],
      uploadDialogActive: false,
      createAlbumDialogActive: false,
      editAlbumDialogActive: false,
      confirmDeletionDialogActive: false,
      viewMediaDialogActive: false,
      viewingAlbum: false,
      activeAlbumMetadata: null,
      userId: null,
      isMyProfile: false,
      isAdminUser: false,
      clickedImageWidth: 0,
      clickedImage: {},
      editAlbumObject: {}
    };
  },

  components: {
    MediaFilter,
    MediaGrid,
    PageHeader,
    MediaUpload,
    AlbumCreate,
    AlbumEdit,
    MediaDialog,
    ConfirmDelete
  },

  computed: {
    /**
     * Media Flow:
     *   1. Fetch Media from the backend, store in  'allMedia'
     *   2. Deep copy  'allMedia'  to  'activeMedia'
     *   3. Organise  'activeMedia'  into separate media categories stored in  'organisedMedia'
     *   4. Count each media item and store the counts in  'mediaCounts'
     *   *** READY STATE ***
     *   5. When a user clicks on an album,  'activeMedia'  is updated to the album content.
     *      This triggers a recalculation for steps 3 and 4 and everything cascades nicely.
     */

    /**
     * Traverses the array of albums fetched from the server and organises them into
     * media categories (photos, videos, albums). This is done so that filtering is trivial.
     * This is recomputed every time activeMedia is updated.
     */
    organisedMedia: function() {
      let organisedMedia = {
        all: [],
        photos: [],
        videos: [],
        albums: []
      };

      let viewingAlbum = this.viewingAlbum;

      this.activeMedia.forEach(function(album) {
        if (!viewingAlbum) organisedMedia.all.push(album);
        if (!viewingAlbum) organisedMedia.albums.push(album);

        album.content.forEach(function(media) {
          organisedMedia.all.push(media);

          if (media.type === "Photo") {
            organisedMedia.photos.push(media);
          } else if (media.type === "Video") {
            organisedMedia.videos.push(media);
          }
        });
      });

      return organisedMedia;
    },

    /**
     * Calculates the number of media items and albums to be displayed in the side filter area.
     * This is recomputed every time organisedMedia is updated.
     */
    mediaCounts: function() {
      let albumCount = 0;
      // If we're not currently viewing an album, set album count to the number of albums
      if (!this.viewingAlbum) {
        albumCount = this.activeMedia.length;
      }

      return {
        all:
          this.organisedMedia.photos.length +
          this.organisedMedia.videos.length +
          albumCount,
        albums: albumCount,
        photos: this.organisedMedia.photos.length,
        videos: this.organisedMedia.videos.length
      };
    },

    /**../../store/index
     * Filters the currently displayed media (organisedMedia) to only display whichever category the user selects
     * This is recomputed every time organisedMedia is updated.
     *
     *  Note,  'filteredMedia'  is simply a function to provide code clarity, it simply filters and returns
     *  the media the user has requested to see. This function is semi-redundant as  'organisedMedia'  performs
     *  a similar function, however I believe it makes things more readable.
     */
    filteredMedia: function() {
      let media = [];

      switch (this.activeFilter) {
        case "All":
          media = this.organisedMedia.albums.concat(
            this.organisedMedia.photos,
            this.organisedMedia.videos
          );
          break;

        case "Albums":
          media = this.organisedMedia.albums;
          break;

        case "Photos":
          media = this.organisedMedia.photos;
          break;

        case "Videos":
          media = this.organisedMedia.videos;
          break;

        default:
          // Show all (albums + media)
          media = this.organisedMedia.albums.concat(
            this.organisedMedia.photos,
            this.organisedMedia.videos
          );
      }

      return media;
    },

    /**
     * Checks if the media/album can be edited
     */
    canCRUDMedia() {
      return this.isAdminUser || this.isMyProfile;
    },

    /**
     * Options used in the header component when viewing all albums.
     */
    normalOptions() {
      if (this.canCRUDMedia) {
        return [
          { action: this.openUploadDialog, icon: "add_photo_alternate" },
          { action: this.openCreateAlbumDialog, icon: "add_to_photos" }
        ];
      } else {
        return [];
      }
    },

    /**
     * Options used in the header component when viewing an album.
     */
    albumOptions() {
      if (this.canCRUDMedia) {
        return [{ action: this.openUploadDialog, icon: "add_photo_alternate" }];
      } else {
        return [];
      }
    }
  },

  methods: {
    deleteAndCloseDialog(deleteAll) {
      this.confirmDeletionDialogActive = false;
      this.deleteMedia(this.clickedImage, deleteAll);
      this.viewMediaDialogActive = false;
    },

    /**
     * Updates the active filter to the provided parameter
     */
    changeFilter(filter) {
      this.activeFilter = filter;
    },

    /**
     * Opens visibility of the media upload dialog.
     */
    openUploadDialog() {
      this.uploadDialogActive = true;
    },

    /**
     * Closes visibility of the media upload dialog.
     */
    closeUploadDialog() {
      this.uploadDialogActive = false;
    },

    /**
     * Opens the visibility of the album creation dialog.
     */
    openCreateAlbumDialog() {
      this.createAlbumDialogActive = true;
    },

    /**
     * Closes visibility of the album creation dialog.
     */
    closeCreateAlbumDialog() {
      this.createAlbumDialogActive = false;
    },

    /**
     * Opens visibility of the album editing dialog.
     */
    openEditAlbumDialog(album) {
      this.editAlbumDialogActive = true;
      this.editAlbumObject = album;
    },

    /**
     * Closes visibility of the album editing dialog.
     */
    closeEditAlbumDialog() {
      this.editAlbumDialogActive = false;
    },

    /**
     * Fetches all albums for a given user id. Sets the all media variable to the response if successful.
     */
    getAllAlbums: function() {
      mediaRepository
        .getUserAlbums(this.userId)
        .then(response => {
          this.allMedia = response.data;
          response.data.forEach(item => {
            this.mediaMap[item.id] = item;
          });
          if (this.viewingAlbum) {
            this.activeMedia = [
              deepCopy(this.mediaMap[this.activeAlbumMetadata.id])
            ];
          } else {
            this.activeMedia = deepCopy(this.allMedia);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Fetches all albums for a given user id. Sets the all media variable to the response if successful.
     */
    createNewAlbum: function(newAlbumName) {
      mediaRepository
        .createAlbum(this.userId, { name: newAlbumName })
        .then(() => {
          this.getAllAlbums();
          this.closeCreateAlbumDialog();
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Updates the album for a given user and album id
     */
    editNewAlbum: function(newAlbumName, albumId) {
      mediaRepository
        .updateAlbum(this.userId, albumId, { name: newAlbumName })
        .then(() => {
          this.getAllAlbums();
        })
        .catch(err => {
          console.log(err);
        });
    },

    /**
     * Handles opening of media grid elements. If an album element is clicked on, the media inside the album is
     * displayed. If a media element is clicked on, a media viewing dialog is displayed.
     */
    openElement(item) {
      // Trying to open an album
      if (item.content) {
        this.viewingAlbum = true;
        this.activeFilter = "All";
        this.activeMedia = [item];
        this.activeAlbumMetadata = item;
      } else {
        // Trying to open an photo/video/media
        const myImage = new Image();
        myImage.src =
          base_url +
          `/api/users/${this.$route.params.id}/media/${item.filename}`;
        this.clickedImageWidth = myImage.width < 400 ? 400 : myImage.width;
        this.clickedImage = item;
        this.viewMediaDialogActive = true;
      }
    },

    /**
     * This method overrides the default router.go(-1) functionality provided by the PageHeader component.
     * It simply updates the user's grid back to viewing all of their albums.
     */
    navigateBack() {
      this.viewingAlbum = false;
      this.activeFilter = "Albums";
      this.activeMedia = deepCopy(this.allMedia);
      this.activeAlbumMetadata = null;
    },

    /**
     * Sends a request to the backend containing formdata with the image to be added to a specified album
     * given an user id and an album id.
     */
    uploadToAlbum(albumId, file) {
      let formData = new FormData();
      formData.append("picture", file);

      mediaRepository
        .uploadMediaToAlbum(this.userId, albumId, formData)
        .then(() => {
          this.getAllAlbums();
        })
        .catch(error => {
          console.log(error);
          this.uploadError = true;
          this.errorText = error.response.data;
        });
    },

    /**
     * Uploads the given media files the backend.
     */
    uploadMedia(files, albums) {
      for (let i = 0; i < files.length; i++) {
        let file = files[i];
        if (albums.length === 0) {
          let albumId = this.$store.getters.getUser.defaultAlbumId;
          this.uploadToAlbum(albumId, file);
        }

        for (let i = 0; i < albums.length; i++) {
          let albumId = albums[i].id;
          this.uploadToAlbum(albumId, file);
        }
      }

      this.closeUploadDialog();
    },

    updateMedia(clickedImage) {
      mediaRepository
        .updateMedia(this.$route.params.id, clickedImage.id, clickedImage)
        .then(() => {
          this.getAllAlbums();
        });
    },

    deleteMedia(clickedImage, deleteAll) {
      mediaRepository
        .deleteMedia(
          this.$route.params.id,
          this.activeAlbumMetadata.id,
          clickedImage.id,
          deleteAll
        )
        .then(() => {
          this.getAllAlbums();
        });
    }
  },

  mounted() {
    this.userId = this.$route.params.id;
    this.destId = this.$route.params.dest_id;

    if (!this.userId) {
      this.userId = this.$store.getters.getUser.id;
    }

    this.isMyProfile = this.$store.getters.getUser.id == this.userId;
    this.isAdminUser = this.$store.getters.getIsUserAdmin;

    this.getAllAlbums();
  }
};
</script>