 
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
            _getDestinations: async function(userId) {
                try {
                    await this.$store.dispatch('getDestinations', {userId});
                } catch (e) {
                    print(e);
                }
            },

            _postDestination: async function(userId, destination) {
                try {
                    return await this.$store.dispatch('postDestination', {userId, destination});
                } catch (e) {
                    print(e);
                    return e;
                }
            },

            _putDestination: async function(userId, destinationId, destination) {
                try {
                    await this.$store.dispatch('putDestination', {userId, destinationId, destination});
                } catch (e) {
                    print(e);
                }
            },

            _deleteDestination: async function(userId, destinationId) {
                try {
                    await this.$store.dispatch('deleteDestination',{userId, destinationId});
                } catch (e) {
                    print(e);
                }
            },

            getDestinationById: function(destId) {
                return this.destinations[destId];
            }


        },
        async mounted() {
            console.log('Created in dest mixin is called and userId =', this.userId);
            await this._getDestinations(this.userId);
        },
    };
</script>
