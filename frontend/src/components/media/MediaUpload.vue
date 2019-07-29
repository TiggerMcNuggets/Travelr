<template>
    <v-card>
        <v-card-title primary-title>
            <h2 class="headline">Upload Media</h2>
        </v-card-title>
        <v-divider></v-divider>

        <v-card-text>
            <div id="dropzone">Drag photos or click to open file explorer.</div>
            <v-flex mt-3>
                <MediaGridWithDelete :media="files" :deletePhoto="deleteImage"/>
            </v-flex>
        </v-card-text>

        <v-divider></v-divider>

        <div class="album-select">

            <v-card-title primary-title>

                <h3 class="media">Select Albums</h3>
            </v-card-title>

            <div>
                <v-layout wrap>

                    <v-flex class="album-element" xs12 md6>

                    <v-select
                            :items="albumNames"
                            label="Select"
                            v-model="selectedAlbumNames"
                            attach
                            multiple
                            chips
                    ></v-select>
                    </v-flex>

                    <v-flex xs12 md4 offset-md2>

                    <v-btn @click="openCreateAlbum">Create new album</v-btn>

                    </v-flex>


                </v-layout>

            </div>


        </div>

        <div class="selected-albums">
            <v-layout row wrap>
                <v-flex v-for="item in selectedAlbums" xs12 sm6 class="album-element">
                    <AlbumElement v-if="item.content" :album="item"></AlbumElement>
                </v-flex>
            </v-layout>


        </div>

        <v-divider></v-divider>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn ma-3 flat v-on:click="toggleUploadDialogue()">Cancel</v-btn>
            <v-btn ma-3 color="primary" flat v-on:click="uploadMedia(rawFiles)">Upload Media</v-btn>
        </v-card-actions>

        <v-dialog v-model="createAlbumDialogActive" width="800">
            <AlbumCreate :toggleCreateAlbumDialogue="toggleCreateAlbumDialogue"></AlbumCreate>
        </v-dialog>

    </v-card>
</template>


<style>

    .album-select {
        width: 100%;
        padding: 0 0 10px 20px
    }

    .selected-albums {
        width: 100%;
        padding: 0 20px 10px 20px
    }

    .album-element {
        padding: 0 10px

    }

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
    import MediaGridWithDelete from "../media/MediaGridWIthDelete";
    import AlbumElement from "./AlbumElement";
    import AlbumCreate from "./AlbumCreate";


    export default {

        components: {
            MediaGridWithDelete,
            AlbumElement,
            AlbumCreate
        },

        props: {
            uploadMedia: Function,
            toggleUploadDialogue: Function,
            allAlbums: Array
        },

        // local variables
        data() {
            return {
                files: [],
                rawFiles: [],
                selectedAlbumNames: [],
                createAlbumDialogActive: false
            };
        },

        methods: {
            /**
             * Deselects the image and removes from the list of selected images.
             * @param selectedImage The image to remove from the selected images.
             */
            deleteImage(selectedImage) {
                for (let i = 0; i <= this.files.length; i++) {
                    if (selectedImage === this.files[i]) {
                        this.files.splice(i, 1);
                        this.rawFiles.splice(i, 1);
                    }
                }
            },

            triggerCallback(e, callback) {
                var files;
                if (e.dataTransfer) {
                    files = e.dataTransfer.files;
                } else if (e.target) {
                    files = e.target.files;
                }
                callback.call(null, files);
            },

            makeDroppable(ele, callback) {
                var input = document.createElement("input");
                input.setAttribute("type", "file");
                input.setAttribute("multiple", true);
                input.style.display = "none";
                input.addEventListener("change", e => {
                    this.triggerCallback(e, callback);
                });
                ele.appendChild(input);

                ele.addEventListener("dragover", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    ele.classList.add("dragover");
                });

                ele.addEventListener("dragleave", function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    ele.classList.remove("dragover");
                });

                ele.addEventListener("drop", e => {
                    e.preventDefault();
                    e.stopPropagation();
                    ele.classList.remove("dragover");
                    this.triggerCallback(e, callback);
                });

                ele.addEventListener("click", function () {
                    input.value = null;
                    input.click();
                });
            },

            /**
             * allows user to create album using dialog
             */
            openCreateAlbum() {
                this.createAlbumDialogActive = !this.createAlbumDialogActive;
            },
        },

        mounted: function () {
            var dropzone = document.getElementById("dropzone");

            this.makeDroppable(dropzone, files => {
                this.rawFiles = files;
                for (let i = 0; i < files.length; i++) {
                    let file = files[i];
                    var reader = new FileReader();

                    reader.onload = e => {
                        this.files.push(e.target.result);
                    };
                    reader.readAsDataURL(file);
                }
            });
        },

        /**
         * Initialises the component with the`image data.
         */
        created: function () {
            this.files = []; // This should actually be done when upload is clicked!
        },

        computed: {
            /**
             * Retrieves the names of all albums
             */
            albumNames() {
                let albumNames = [];
                for (let i = 0; i < this.allAlbums.length; i++) {
                    albumNames.push(this.allAlbums[i].name);
                }
                return albumNames;
            },

            /**
             * computes list of all albums that have been selected
             */
            selectedAlbums() {
                let selectedAlbums = [];
                for (let i = 0; i < this.allAlbums.length; i++) {
                    console.log(this)
                    if (this.selectedAlbumNames.includes(this.allAlbums[i].name)) {
                        selectedAlbums.push(this.allAlbums[i]);
                    }
                }
                return selectedAlbums;
            },
        },

    };
</script>
