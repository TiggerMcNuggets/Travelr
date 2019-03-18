/* eslint-disable */

<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
      </label>
        <button v-on:click="submitFile()">Submit</button>
    </div>
  </div>
</template>


<style>

</style>


<script>
import { store } from "../../store/index";
import { storeImage } from "../../repository/PersonalPhotosRepository";

export default {
  store,
  // local variables
  data() {
    return {
        file: ''
    //   showCreateDestination: false,
    //   showEditDestination: false
    };
  },
  // the place where you want to make the store values readable
//   computed: {
//     destinations() {
//       return store.state.destinations.destinations;
//     }
//   },
  // child components
//   components: {
//     DestinationCreate: DestinationCreate
//   },
  methods: {
      handleFileUpload() {
          this.file = this.$refs.file.files[0];
      },
      submitFile() {
          let formData = new FormData();
          formData.append('picture', this.file);
          console.log(formData);
          storeImage(this.$route.params.id, formData);
      }
    }
//   },
//   created: async function() {
//     // committing to the store like this allows you to trigger the setDestinations mutation you can find in the destinations module for the store
//     store.commit("setDestinations");
//   }
};
</script>
