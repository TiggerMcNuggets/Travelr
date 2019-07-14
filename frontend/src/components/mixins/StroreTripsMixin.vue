
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
            }
        },

        methods: {

            /**
             * @param userId
             * @returns {Promise<void>}
             */
            _getTrips: async function(userId) {
                try {
                    await this.$store.dispatch('getTrips', {userId});
                } catch (e) {
                    print(e);
                }
            },

            _postTrip: async function(userId, trip) {
                try {
                    return await this.$store.dispatch('postTrip', {userId, trip});
                } catch (e) {
                    print(e);
                    return e;
                }
            },

            _putTrip: async function(userId, tripId, trip) {
                try {
                    await this.$store.dispatch('putTrip', {userId, tripId, trip});
                } catch (e) {
                    print(e);
                }
            },

            _deleteTrip: async function(userId, tripId) {
                try {
                    await this.$store.dispatch('deleteTrip',{userId, tripId});
                } catch (e) {
                    print(e);
                }
            },


        },
        async mounted() {
            console.log('Created in dest mixin is called and userId =', this.userId);
            await this._getTrips(this.userId);
        },
    };
</script>
