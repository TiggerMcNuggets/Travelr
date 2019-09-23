<script>
    /**
     * Mixin that allows you to display notifcation mixins from anywhere in the application
     * 
     * Settings for the Snackbar are managed in the Snackbar.vue file
     */
    import { store } from '../../store/index';

    export default {
        store,
        methods: {
          showErrorSnackbar: function(text) {
            this._showSnackbar(text, "red")
          },
          showInfoSnackbar: function(text) {
            this._showSnackbar(text, "blue")
          },
          showSuccesssSnackbar: function(text) {
            this._showSnackbar(text, "green")
          },
          _showSnackbar: function(text, color) {
            this.$store.dispatch("setSnackbar", {text:text,color:color})
          }          
        }        
    };
</script>
