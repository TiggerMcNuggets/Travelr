
<script>
    import { store } from '../../store/index';
    import { mapActions } from 'vuex'


    /**
     * Mixin to be included anywhere there is need for destinations for a specific user
     */
    export default {
        el: "#store_destinations_mixin",
        store,
        data() {
            return {
            };
        },

        computed: {
        /**
         * @returns the destinations of a user
         */
          destinations() {
              return this.$store.state.destinations;
          }
        },

        created() {
        },
        methods: {

            getDestinations: async function(userId) {
                try {
                    await this.$store.dispatch('getDestinations', userId);
                } catch (e) {
                    print(e);
                }
            },

            postDestination: async function(userId, destination) {
                try {
                    await this.$store.dispatch('postDestination', userId, destination);
                } catch (e) {
                    print(e);
                }
            },

            putDestination: async function(userId, destinationId) {
                try {
                    await this.$store.dispatch('putDestination', userId, destinationId);
                } catch (e) {
                    print(e);
                }
            },

            deleteDestination: async function(userId, destinationId) {
                try {
                    await this.$store.dispatch('postDestination', userId, destinationId);
                } catch (e) {
                    print(e);
                }
            },



        }
    };
</script>
