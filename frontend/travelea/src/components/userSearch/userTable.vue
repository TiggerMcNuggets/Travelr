<template>
    <v-card>
            <v-card-title>
            Profiles
            <v-spacer></v-spacer>
            <v-text-field
                v-model="search"
                append-icon="search"
                label="Search"
                single-line
                hide-details
            ></v-text-field>
            </v-card-title>
                <v-data-table
                :headers="getColumns"
                :items="users"
                :search="search"
                >
                <template v-slot:items="props">
                    <td @click="goToUser(props.item.id)" class="text-xs-right">{{ props.item.firstName }}</td>
                    <td class="text-xs-right">{{ props.item.lastName }}</td>
                    <td class="text-xs-right">{{ props.item.dateOfBirth }}</td>
                    <td class="text-xs-right">{{ props.item.gender }}</td>
                    <td>
                        <ul style="list-style-type:none">
                            <li v-for="(nationality, index) in props.item.nationalities" :key="index">
                                {{ nationality.name }} <br/>
                            </li>
                        </ul>
                    </td>
                    <td>
                        <ul style="list-style-type:none">
                            <li v-for="(travelType, index) in props.item.travellerTypes" :key="index">
                                {{ travelType.name }} <br/>
                            </li>
                        </ul>
                    </td>
                    <td v-if="isAdmin" class="text-xs-right">
                        <v-btn flat icon color="red lighten-2" v-on:click="deleteUser(props.item.id)">
                            <v-icon>delete</v-icon>
                        </v-btn>
                    </td>
                </template>
                </v-data-table>
        </v-card>
</template>


<script>
import { store } from "../../store/index";

export default {
    store,
    data () {
        return {   
            search: '',
            isAdmin: false
        }
    },
    // the place where you want to make the store values readable
    computed: {
        users() {
            return store.state.users.users;
        },
        getColumns() {
            const columns = [{text: 'First Name', value: 'firstName', align: 'left', sortable: true},
                            {text: 'Last Name', value: 'lastName', align: 'left', sortable: true},
                            {text: 'DOB', value: 'dateOfBirth', align: 'left', sortable: true},
                            {text: 'Gender', value: 'gender', align: 'left', sortable: true},
                            {text: 'Nationalities', value: 'nationalities', align: 'left', sortable: true},
                            {text: 'Traveller Types', value: 'types', align: 'left', sortable: true}];
            if (this.isAdmin) {
                columns.push({text: 'Delete', align: 'left', sortable: false})
            }
            return columns;
        },
    },
    methods: {
        async deleteUser(userId) {
            await store.dispatch("deleteUser", userId);
            await store.dispatch("getUsers", false);
        },
        goToUser(id) {
            var endpoint = '/user/' + id
            this.$router.push(endpoint)
        }
    },
    created: async function() {
        await store.dispatch("getUsers", false);
        if (store.getters.getIsUserAdmin) {
            this.isAdmin = true;
        }
    }
}
</script>
