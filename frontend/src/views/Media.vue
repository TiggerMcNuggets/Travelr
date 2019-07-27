<template>
    <v-layout d-block pl-4 pt-3>
        <PageHeader title="Media" disableUndoRedo :options="options"/>
        <v-layout row wrap>
            <MediaFilter :changeFilter="changeFilter" :mediaCounts="mediaCounts"></MediaFilter>
            <MediaGrid :filteredMedia="filteredMedia"></MediaGrid>
        </v-layout>

        <v-dialog v-model="uploadDialogActive" width="800">
            <MediaUpload :uploadImages="uploadMedia" :toggleUploadDialogue="toggleUploadDialogue"></MediaUpload>
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
    import MediaUpload from "../components/photos/PhotoUpload";
    import AlbumCreate from "../components/media/AlbumCreate";
	import {temp} from "../components/media/temp";

	export default {
		name: "Media",

		data() {
			return {
				activeFilter: 0,
				albums: temp,
                uploadDialogActive: false,
                createAlbumDialogActive: false
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
			organisedMedia: function () {
				let organisedMedia = {
					"images": [],
					"videos": [],
					"albums": []
				};

				this.albums.forEach(function (album) {
					organisedMedia.albums.push(album);
					album.content.forEach(function (media) {
						if (media.type === "image") {
							organisedMedia.images.push(media);
						} else if (media.type === "video") {
							organisedMedia.videos.push(media);
						}
					});
				});

				return organisedMedia;
			},

			mediaCounts: function () {
				return {
					"all": this.organisedMedia.images.length + this.organisedMedia.videos.length + this.albums.length,
					"albums": this.albums.length,
					"images": this.organisedMedia.images.length,
					"videos": this.organisedMedia.videos.length
				};
			},

			filteredMedia: function () {
				let media = [];
				let albumsFilter = 1;
				let photosFilter = 2;
				let videosFilter = 3;

				switch (this.activeFilter) {
					case albumsFilter:
						media = this.organisedMedia.albums;
						break;

					case photosFilter:
						media = this.organisedMedia.images;
						break;

					case videosFilter:
						media = this.organisedMedia.videos;
						break;

					// Show all (albums + media)
					default:
						media = this.organisedMedia.albums.concat(this.organisedMedia.images, this.organisedMedia.videos);
				}

				return media;
			},

			options() {
				return [
					{action: this.toggleUploadDialogue, icon: "add_photo_alternate"},
					{action: this.toggleCreateAlbumDialogue, icon: "add_to_photos"}
				];
			}
		},

		methods: {
			changeFilter(num) {
				this.activeFilter = num;
			},

            toggleUploadDialogue() {
                this.uploadDialogActive = !this.uploadDialogActive;
            },

            toggleCreateAlbumDialogue() {
                this.createAlbumDialogActive = !this.createAlbumDialogActive;
            },

            uploadMedia() {
                this.toggleUploadDialogue();
            }
		}

	}
</script>