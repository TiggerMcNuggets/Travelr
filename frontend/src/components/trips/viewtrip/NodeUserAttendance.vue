<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

    <div class="top-attendance-info">
        <div class="mb-3 d-flex align-center">
            <div class="d-flex">
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-chip v-if=isGoing v-on="on" color="success lighten-5">
                            <v-icon
                                    color="success"
                                    left>
                                check_circle
                            </v-icon>
                            Going
                        </v-chip>
                    </template>
                    <span>{{getGoingUsersString}}</span>
                </v-tooltip>
            </div>
            <div v-if="!isGoing">
                <v-tooltip top>
                    <template v-slot:activator="{ on}">
                        <v-icon
                                v-on="on"
                                v-on:click="() => changeStatus(node, statuses.going)"
                                left>
                            check_circle
                        </v-icon>
                    </template>
                    <span>{{getGoingUsersString}}</span>
                </v-tooltip>
            </div>


            <div>
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-chip v-on="on" v-if="isInterested" color="amber lighten-4">
                            <v-icon
                                    color="amber darken-1"
                                    left>
                                help
                            </v-icon>
                            Interested
                        </v-chip>
                    </template>
                    <span>{{getInterestedUsersString}}</span>
                </v-tooltip>
            </div>

            <v-tooltip top>
                <template v-slot:activator="{ on}">
                    <v-icon
                            v-on="on"
                            v-if="!isInterested"
                            v-on:click="() => changeStatus(node, statuses.maybe)"
                            left>
                        help
                    </v-icon>
                </template>
                <span>
                    {{getInterestedUsersString}}
                </span>
            </v-tooltip>

            <v-tooltip top>
                <template v-slot:activator="{ on}">
                    <v-chip v-on="on" v-if="isNotGoing" color="error lighten-5">
                        <v-icon
                                color="error"
                                left>
                            cancel
                        </v-icon>
                        Not going
                    </v-chip>
                </template>
                <span>{{getNotGoingUsersString}}</span>
            </v-tooltip>

            <v-tooltip top>
                <template v-on="on" v-slot:activator="{ on}">
                    <v-icon
                            v-on="on"
                            v-if="!isNotGoing"
                            v-on:click="() => changeStatus(node, statuses.notGoing)"
                            left>
                        cancel
                    </v-icon>
                </template>
                <span>{{getNotGoingUsersString}}</span>
            </v-tooltip>

        </div>
    </div>
</template>

<style>
    .top-attendance-info {
        display: flex;
        justify-content: space-between;
    }
</style>


<script>
    import { RepositoryFactory}  from "../../../repository/RepositoryFactory";
    let tripRepository = RepositoryFactory.get("trip");
    import StoreTripsMixin from "../../mixins/StoreTripsMixin";


    export default {
        name: "NodeUserAttendance",

        mixins: [StoreTripsMixin],

        props: {
            node: Object,
        },

        data() {
            return {
                userId: this.$route.params.id,
                statuses: {
                    notGoing: 'NOT_GOING',
                    going: 'GOING',
                    maybe: 'MAYBE'
                }
            };
        },

        computed: {

            /**
             */
            userNodeStatus() {
                const status = {status: undefined};
                const nodeUserStatus = this.node.usergroup.find(u => {
                    return parseInt(u.userId) === parseInt(this.userId);
                });
                if (!nodeUserStatus) return status;
                return {...status, status: nodeUserStatus.status};
            },

            /**
             * Returns if the user is going to a specific trip node
             */
            isGoing() {
                return this.userNodeStatus.status === this.statuses.going
            },

            /**
             * Returns if the user is going to a specific trip node
             */
            isInterested() {
                return this.userNodeStatus.status === this.statuses.maybe
            },

            /**
             * Returns if the user is going to a specific trip node
             */
            isNotGoing() {
                return this.userNodeStatus.status === this.statuses.notGoing
            },

            /**
             * Gets a string summarising all the people that are going to a node
             */
            getGoingUsersString() {
                return this._getListOfUsersForGivenStatus(this.statuses.going);
            },

            /**
             * Gets a string summarising all the people that are not going to a node
             */
            getNotGoingUsersString() {
                return this._getListOfUsersForGivenStatus(this.statuses.notGoing);
            },

            /**
             * Gets a string summarising all the people that are interested in a node
             */
            getInterestedUsersString() {
                return this._getListOfUsersForGivenStatus(this.statuses.maybe);

            },

        },

        methods: {


            _getListOfUsersForGivenStatus(status) {
                let names = "";
                this.node.usergroup.forEach((u) => {
                    if (u.status === status) {
                        names = `${names}${u.firstName} ${u.lastName}, `;
                    }
                });
                if (names.length === 0) return "Nobody";
                return names.substring(0, names.length - 2);
            },

            /**
             * Sends a request for changing status.
             */
            changeStatus(node, status) {
                let payload = { status: status };
                tripRepository
                    .updateGroupTripStatus(this.userId, node.id, payload)
                    .then(() => {
                        this._getTrip(this.$store.getters.getUser.id, this.tripId);
                    });
            },
        },
    };
</script>