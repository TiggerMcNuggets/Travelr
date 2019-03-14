<template>
    <v-card>
        <v-container fluid>
            <v-layout wrap>
                <v-combobox
                v-model="select"
                chips
                label="First Name"
                ></v-combobox>
                <v-combobox
                v-model="select"
                chips
                label="Last Name"
                ></v-combobox>
                <v-layout row wrap>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                            label="Min Age"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-text-field
                            label="Max Age"
                        ></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-combobox
                        v-model="select"
                        chips
                        label="Gender"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm6 md6>
                        <v-combobox
                        v-model="select"
                        chips
                        label="Nationality"
                        ></v-combobox>
                    </v-flex>
                    <v-flex xs12 sm12 md12>
                        <v-select
                        v-model="value"
                        :items="travelTypes"
                        attach
                        chips
                        multiple
                        label="Traveller Type"
                        ></v-select>
                    </v-flex>
                </v-layout>
                
            </v-layout>
        </v-container>
    </v-card>
</template>

<script>
export default {
}
</script>