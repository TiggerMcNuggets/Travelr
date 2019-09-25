<script>
    /**
     * Mixin that allows you to display notifcation mixins from anywhere in the application
     * 
     * Settings for the Snackbar are managed in the Snackbar.vue file
     */
    import { store } from '../../store/index';

    export default {
        store,

        data() {
            return {
            }
        },

        computed: {
            _snackbarMessages() {
                return {
                    tripCreateSuccess: "Trip created successfully!",
                    tripCreateFail: "Failed to create trip",

                    destinationCreateSuccess: "Destination created successfully!",
                    destinationCreateFail: "Failed to create the destination",

                    destinationUpdateSuccess: "Destination updated successfully!",
                    destinationUpdateFail: "Failed to update the destination",

                    groupCreateSuccess: "Group created successfully!",
                    groupCreateFail: "Failed to create the group",

                    groupUserDeleteSuccess: "User deleted from group successfully!",
                    groupUserDeleteFail: "Failed to delete user from group",

                    groupUserAddSuccess: "User added to the group successfully!",
                    groupUserAddFail: "Failed to add the user to the group",

                    emailMePdfAndIcalSuccess: "PDF and iCal file from this trip have been sent successfully to you",
                    emailGroupPdfAndIcalSuccess: "PDF and iCal file from this trip have been sent successfully to everyone in the group",
                    failedToSendEmail: "Failed to send email",
                };
            }
        },

        /**
         * All public methods rely on params coming from you.
         * text (NO DEFAULT): string, register this in the _snackbarMesssages computed value
         * time (DEFAULT 3500ms): number, overload method if you want to change this
         */
        methods: {
          showErrorSnackbar: function(text, time) {
            this._showSnackbar(text, "red", time)
          },
          showInfoSnackbar: function(text, time) {
            this._showSnackbar(text, "blue", time)
          },
          showSuccessSnackbar: function(text, time) {
            this._showSnackbar(text, "green", time)
          },
          _showSnackbar: function(text, color, time) {
            this.$store.dispatch("setSnackbar", {text:text,color:color, time: time})
          }          
        }        
    };

</script>
