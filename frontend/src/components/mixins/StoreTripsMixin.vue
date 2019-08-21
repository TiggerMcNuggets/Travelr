
<script>
    import { store } from '../../store/index';

    /**
     * Mixin to be included anywhere there is need for trips for a specific user
     */
    export default {
        store,

        computed: {

            /**
             * @returns the trips of a user
             */
            trips() {
                return this.$store.getters.getTrips;
            },
            selectedTrip() {
                return this.$store.getters.getSelectedTrip;
            }
        },

        methods: {

            /**
             * Gets a list of user's trips and saves to store
             * @param userId The user's id
             * @returns {Promise<void>}
             */
            _getTrips: async function(userId) {
                try {
                    await this.$store.dispatch('getTrips', {userId});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Gets a user's trip and saves to the store
             * @param userId The user's id
             * @param tripId The trip's id
             * @returns {Promise<void>}
             */
            _getTrip: async function(userId, tripId) {
                try {
                    return await this.$store.dispatch('getTrip', {userId, tripId});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Creates a trip for a user and saves to store
             * @param userId The user's id
             * @param trip The trip object
             * @returns {Promise<void>}
             */
            _postTrip: async function(userId, trip) {
                try {
                    return await this.$store.dispatch('postTrip', {userId, trip});
                } catch (e) {
                    console.log(e);
                    return e;
                }
            },

            /**
             * Updates a trip for a user and saves to store
             * @param userId The user's id
             * @param tripId The trip's id
             * @param trip The trip object
             * @returns {Promise<void>}
             */
            _putTrip: async function(userId, tripId, trip) {
                try {
                    await this.$store.dispatch('putTrip', {userId, tripId, trip});
                } catch (e) {
                    console.log(e);
                }
            },

            /**
             * Deletes a trip for a user and saves to store
             * @param userId The user's id
             * @param tripId The trip's id
             * @returns {Promise<void>}
             */
            _deleteTrip: async function(userId, tripId) {
                try {
                    await this.$store.dispatch('deleteTrip',{userId, tripId});
                } catch (e) {
                    console.log(e);
                }
            },


        },
        async mounted() {
            await this._getTrips(this.userId);
        },
    };
</script>
