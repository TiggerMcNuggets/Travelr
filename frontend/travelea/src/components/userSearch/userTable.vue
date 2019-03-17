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
            :headers="[
            {text: 'First Name', value: 'fname', align: 'left', sortable: true},
            {text: 'Last Name', value: 'lname', align: 'left', sortable: true},
            {text: 'DOB', value: 'dob', align: 'left', sortable: true},
            {text: 'Gender', value: 'gender', align: 'left', sortable: true},
            {text: 'Natoionalities', value: 'nationalities', align: 'left', sortable: true},
            {text: 'Traveller Types', value: 'types', align: 'left', sortable: true}]"
            :items="users"
            :search="search"
            >
            <template v-slot:items="props">
                <td class="text-xs-right">{{ props.item.fname }}</td>
                <td class="text-xs-right">{{ props.item.lname }}</td>
                <td class="text-xs-right">{{ props.item.dob }}</td>
                <td class="text-xs-right">{{ props.item.gender }}</td>
                <td class="text-xs-right">{{ props.item.nationalities }}</td>
                <td class="text-xs-right">{{ props.item.types }}</td>
            </template>
            <v-alert v-slot:no-results :value="true" color="error" icon="warning">
                Your search for "{{ search }}" found no results.
            </v-alert>
            </v-data-table>
        </v-card>
</template>


<script>
import { store } from "../../store/index";
import userTable from "./userTable"
import searchFilter from "./searchFilter"

export default {
    data () {
        return {   
            search: ''
    }
    },
    components: {
        userTable,
        searchFilter
    },
    // the place where you want to make the store values readable
    computed: {
        users() {
        return store.state.users.users;
        }
    },
    created: async function() {
        store.commit("setUsers");
    }
}
</script>
