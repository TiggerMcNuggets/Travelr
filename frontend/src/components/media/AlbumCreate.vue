<template>
  <v-card>
    <v-card-title primary-title>
      <h2 class="headline">Create an Album</h2>
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text>
      <v-layout row wrap>
        <v-flex xs12 ml-3 mr-3>
          <v-text-field
            label="Title"
            v-model="newAlbumName"
            maxlength="50"
          ></v-text-field>
        </v-flex>
      </v-layout>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn ma-3 flat v-on:click="toggleCreateAlbumDialogue()">Cancel</v-btn>
      <v-btn ma-3 color="primary" flat v-on:click="createNewAlbum(newAlbumName)">Create Album</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
	export default {
		props: {
			toggleCreateAlbumDialogue: Function,
      createNewAlbum: Function
		},

		data() {
			return {
				publicSwitch: false,
        newAlbumName: ""
			};
		},

		methods: {

		}

	};
</script>
