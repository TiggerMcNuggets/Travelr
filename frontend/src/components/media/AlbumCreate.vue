<template>
    <v-card>
        <v-card-title primary-title>
            <h2 class="headline">Create an Album</h2>
        </v-card-title>
        <v-divider></v-divider>

        <v-card-text>
            <v-layout row wrap>
                <v-flex ml-4 md3>
                    <v-text-field
                        label="Title"
                        :value="albumTitle"
                        maxlength="50"
                    ></v-text-field>
                </v-flex>
                <v-flex ml-4 md6>
                    <v-text-field
                            label="Short Description"
                            :value="albumDescription"
                            maxlength="50"
                    ></v-text-field>
                </v-flex>
                <v-flex ml-3>
                    <v-switch
                        v-model="publicSwitch"
                        label="Private"
                    ></v-switch>
                </v-flex>
            </v-layout>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn ma-3 flat v-on:click="toggleCreateAlbumDialogue()">Cancel</v-btn>
            <v-btn ma-3 color="primary" flat v-on:click="createAlbum()">Create Album</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
  export default {
    props: {
      toggleCreateAlbumDialogue: Function
    },

    data() {
      return {
        publicSwitch: false,
        albumTitle: "",
        albumDescription: ""
      };
    },

    methods: {
      createAlbum() {
        console.log("IMPLEMENT ME!");
      }
    }

  };
</script>
