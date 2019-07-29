<template>
  <v-layout d-block pl-4 pt-3>
    <PageHeader v-if="viewingAlbum" :title="activeAlbumMetadata.name" disableUndoRedo enableBackButton :backButtonOverride="navigateBack" :options="options"/>
    <PageHeader v-else title="Media" disableUndoRedo enableBackButton :options="options"/>

    <v-layout row wrap>
      <v-flex xs2 v-if="viewingAlbum">
        <MediaFilter :changeFilter="changeFilter" :mediaCounts="mediaCounts"></MediaFilter>
      </v-flex>
      <v-flex xs10 v-if="viewingAlbum">
        <MediaGrid :filteredMedia="filteredMedia" :openElement="openElement"></MediaGrid>
      </v-flex>
      <v-flex xs12 v-else>
        <MediaGrid :filteredMedia="filteredMedia" :openElement="openElement"></MediaGrid>
      </v-flex>
    </v-layout>

    <v-dialog v-model="uploadDialogActive" width="800">
      <MediaUpload :uploadMedia="uploadMedia" :toggleUploadDialogue="toggleUploadDialogue" :allAlbums="this.organisedMedia.albums"></MediaUpload>
    </v-dialog>

    <v-dialog v-model="createAlbumDialogActive" width="800">
      <AlbumCreate :toggleCreateAlbumDialogue="toggleCreateAlbumDialogue"></AlbumCreate>
    </v-dialog>

  </v-layout>
</template>

<script>
	import MediaFilter from "../components/media/MediaFilter";
	import MediaGrid from "../components/media/MediaGrid";
	import PageHeader from "../components/common/header/PageHeader";
	import MediaUpload from "../components/media/MediaUpload";
	import AlbumCreate from "../components/media/AlbumCreate";
	import {temp} from "../components/media/temp";
	import {deepCopy} from "../tools/deepCopy"

	export default {
		name: "Media",

		data() {
			return {
				activeFilter: "Albums",
				allMedia: temp,
				activeMedia: [],
				uploadDialogActive: false,
				createAlbumDialogActive: false,
				viewingAlbum: false,
				activeAlbumMetadata: null
			}
		},

		components: {
			MediaFilter,
			MediaGrid,
			PageHeader,
			MediaUpload,
			AlbumCreate
		},

		computed: {

			/**
			 * Media Flow:
			 *   1. Fetch Media from the backend, store in  'allMedia'
			 *   2. Deep copy  'allMedia'  to  'activeMedia'
			 *   3. Organise  'activeMedia'  into separate media categories stored in  'organisedMedia'
			 *   4. Count each media item and store the counts in  'mediaCounts'
			 *   *** READY STATE ***
			 *   5. When a user clicks on an album,  'activeMedia'  is update to the album content.
			 *      This triggers a recalculation for steps 3 and 4 and everything cascades nicely.
			 */

			/**
			 * Traverses the array of albums fetched from the server and organises them into
			 * media categories (photos, videos, albums). This is done so that filtering is trivial.
			 * This is recomputed every time activeMedia is updated.
			 */
			organisedMedia: function () {
				let organisedMedia = {
					"all": [],
					"photos": [],
					"videos": [],
					"albums": []
				};

				let viewingAlbum = this.viewingAlbum;

				this.activeMedia.forEach(function (album) {
					if (!viewingAlbum) organisedMedia.all.push(album);
					if (!viewingAlbum) organisedMedia.albums.push(album);

					album.content.forEach(function (media) {
						organisedMedia.all.push(media);

						if (media.type === "photo") {
							organisedMedia.photos.push(media);
						} else if (media.type === "video") {
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
			mediaCounts: function () {
				let albumCount = 0;
				// If we're not currently viewing an album, set album count to the number of albums
				if (!this.viewingAlbum) {
					albumCount = this.activeMedia.length
				}

				return {
					"all": this.organisedMedia.photos.length + this.organisedMedia.videos.length + albumCount,
					"albums": albumCount,
					"photos": this.organisedMedia.photos.length,
					"videos": this.organisedMedia.videos.length
				};
			},

			/**
			 * Filters the currently displayed media (organisedMedia) to only display whichever category the user selects
			 * This is recomputed every time organisedMedia is updated.
			 *
			 *  Note,  'filteredMedia'  is simply a function to provide code clarity, it simply filters and returns
			 *  the media the user has requested to see. This function is semi-redundant as  'organisedMedia' performs
			 *  a similar function, however I believe it makes things more readable.
			 */
			filteredMedia: function () {
				let media = [];

				switch (this.activeFilter) {
					case "All":
						media = this.organisedMedia.albums.concat(this.organisedMedia.photos, this.organisedMedia.videos);
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
						media = this.organisedMedia.albums.concat(this.organisedMedia.photos, this.organisedMedia.videos);
				}

				return media;
			},

			/**
			 * Options used in the header component.
			 */
			options() {
				return [
					{action: this.toggleUploadDialogue, icon: "add_photo_alternate"},
					{action: this.toggleCreateAlbumDialogue, icon: "add_to_photos"}
				];
			}
		},

		methods: {
			changeFilter(filter) {
				this.activeFilter = filter;
			},

			toggleUploadDialogue() {
				this.uploadDialogActive = !this.uploadDialogActive;
			},

			toggleCreateAlbumDialogue() {
				this.createAlbumDialogActive = !this.createAlbumDialogActive;
			},

			uploadMedia() {
				this.toggleUploadDialogue();
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
					// TODO: Implement this
					console.log("IMPLEMENT ME!")
				}
			},

			navigateBack() {
				this.viewingAlbum = false;
				this.activeFilter = "Albums";
				this.activeMedia = deepCopy(this.allMedia);
				this.activeAlbumMetadata = null;
			}
		},

    mounted() {
			this.activeMedia = deepCopy(this.allMedia);
		}

	}
</script>