<template>
    <v-card>
        <v-container fluid>
            <v-layout wrap>
                <v-layout row wrap>
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model="fName"
                        label="First Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                         <v-text-field
                        v-model="lName"
                        label="Last Name"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model="minAge"
                        label="Min Age"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                        v-model="maxAge"
                        label="Max Age"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-select
                        v-model="gender"
                        :items="['Male', 'Female']"
                        attach
                        chips
                        label="Gender"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-combobox
                        v-model="nationality"
                        :items="['Traveller1', 'Traveller2', 'Traveller3']"
                        chips
                        label="Nationality"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="travellerTypes"
                        :items="['Traveller1', 'Traveller2', 'Traveller3']"
                        attach
                        chips
                        multiple
                        label="Traveller Type"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12 >
                        <v-select
                        v-model="orderBy"
                        :items="['None', 'Nationality', 'Traveller Type']"
                        attach
                        label="Order By"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 sm12 md12 class="text-xs-center">
                        <v-btn color='primary'>Search</v-btn>
                    </v-flex>
                </v-layout>
            </v-layout>
        </v-container>
    </v-card>
</template>

<script>
export default {
    data() {
        return {
            fName: null,
            lName: null,
            minAge: null,
            maxAge: null,
            gender: null,
            nationality: null,
            travellerTypes: null,
            orderBy: null
        }
    }
}
</script>