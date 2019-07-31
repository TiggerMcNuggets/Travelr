 
<script>
    import { store } from '../../store/index';

    /**
     * Mixin to be included anywhere there is need for destinations for a specific user
     */
    export default {
        store,

        computed: {

            /**
             * @returns the destinations of a user
             */
              destinations() {
                  return this.$store.getters.getDestinations;
              }
        },

        methods: {
            /**
             * Sends request to get all destinations and saves it to the store
             * @param userId The user's id
             * @returns {Promise<void>}
             */
            _getDestinations: async function(userId) {
                try {
                    await this.$store.dispatch('getDestinations', {userId});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Creates a new destination
             * @param userId The user's id
             * @param destination The destination body
             * @returns {Promise<void>}
             */
            _postDestination: async function(userId, destination) {
                try {
                    return await this.$store.dispatch('postDestination', {userId, destination});
                } catch (e) {
                    console.log(e);
                    return e;
                }
            },

            /**
             * Updates a destination
             * @param userId The user's id
             * @param destinationId The destination's id
             * @param destination The destination body
             * @returns {Promise<void>}
             */
            _putDestination: async function(userId, destinationId, destination) {
                try {
                    await this.$store.dispatch('putDestination', {userId, destinationId, destination});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Deletes a destination
             * @param userId The user's id
             * @param destinationId The destination's id
             * @returns {Promise<void>}
             */
            _deleteDestination: async function(userId, destinationId) {
                try {
                    await this.$store.dispatch('deleteDestination',{userId, destinationId});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Gets a destination by its id
             * @param destId The destination's id
             * @returns The destination object
             */
            getDestinationById: function(destId) {
                return this.destinations[destId];
            }


        },
        async mounted() {
            await this._getDestinations(this.userId);
        },
    };
</script>
