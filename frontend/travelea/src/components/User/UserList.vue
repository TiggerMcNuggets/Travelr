<template>
    <v-data-table
        :headers="headers"
        :items="userList"
        class="elevation-1"
    >
        <template v-slot:items="props">
            <td>{{ props.item.first_name }}</td>
            <td>{{ props.item.date_of_birth }}</td>
            <td>{{ props.item.gender }}</td>
            <td>{{ props.item.nationality }}</td>
            <td>{{ props.item.passport_country }}</td>
            <td>{{ props.item.traveller_type }}</td>
        </template>
    </v-data-table>
</template>

<script>

export default {
    data () {
        return {
            userList: [],
            headers: [
          { text: 'Name', align: "left", value: 'first_name' },
          { text: 'Birthday',  align: "left",value: 'date_of_birth' },
          { text: 'Gender',  align: "left",value: 'gender' },
          { text: 'Nationality',  align: "left",value: 'nationality' },
          { text: 'Passport Country',  align: "left",value: 'passport_country' },
          { text: 'Traveller Type', align: "left", value: 'backpacker' }
        ],
    }
    },
    created: function() {
        this.userList = UserService.getUserList();
    }
}
</script>
