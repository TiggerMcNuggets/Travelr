<template>
  <v-snackbar :color="color" v-model="show" :timeout="timeout" right bottom>
    {{message}}
    <v-btn flat color="white" @click.native="show = false">Close</v-btn>
  </v-snackbar>
</template>

<style>
</style>

<script>
export default {
  data () {
    return {
      show: false,
      message: 'If you can see this message something broke!',
      color: 'red',
      timeout: 3500, // Use this to change global snackbar display time
      defaultTimeout: 3500,
    }
  },
  /**
   * Watch for changes in the Snackbar Store to update SNACKBAR.
   */
  created: function () {
    this.$store.watch(state => state.snackbar.text, () => {
      const msg = this.$store.getters.getSnackbarText;
      if (msg !== '') {        
        this.message = this.$store.getters.getSnackbarText;
        this.color = this.$store.getters.getSnackbarColor;
        this.timeout = this.$store.getters.getSnackbarTime ? this.$store.getters.getSnackbarTime : this.defaultTimeout;
        this.show = true;
        this.$store.dispatch("resetSnackbar");
      }
    })
  }  
}
</script>