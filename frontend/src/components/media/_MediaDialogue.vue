<template>
    <v-dialog v-model="dialog" :width="clickedImageWidth">
        <v-card>
            <v-img :src="clickedImageURL"></v-img>

            <v-card-title primary-title>
                <div>
                    <h5 class="headline mb-0">Image Name</h5>
                    <div>Description/Other meta info</div>
                </div>
            </v-card-title>

            <v-divider></v-divider>


            <v-card-actions>
                <!-- <v-spacer></v-spacer> -->
                <div v-if="isAdminUser || isMyProfile" class="photo-popup-options">
                    <v-switch v-model="publicPhotoSwitch" :label="`Public Photo`"></v-switch>

                    <v-btn @click="updatePhotoVisability">Apply changes</v-btn>
                    <v-btn @click="setProfilePhoto">Set Profile Photo</v-btn>
                </div>
            </v-card-actions>
            <v-card-actions>
                <v-btn color="red" @click="dialog = false">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style>
    @import "../../assets/css/style.css";
</style>

<script>
  import {
    setProfilePic,
    updatePersonalPhoto
  } from "../../repository/PersonalPhotosRepository";

  export default {
    name: "MediaDialog",

    props: {},

    components: {},

    data() {
      return {
        dialog: false,
        publicPhotoSwitch: false,
        isMyProfile: false,
        isAdminUser: false,
      }
    },

    methods: {
      /**
       * Updates whether the photo is public or private depending on the switch state.
       */
      updatePhotoVisability() {
        this.clickedImage.is_public = this.publicPhotoSwitch;
        updatePersonalPhoto(this.clickedImage);
      },

      /**
       * Sets the user's profile photo as the selected
       */
      setProfilePhoto() {
        setProfilePic(this.id, {
          photo_filename: this.clickedImage.photo_filename
        }).then(() => {
          window.location = "/user/" + this.id + "/photos";
        });
      },
    }
  }
</script>