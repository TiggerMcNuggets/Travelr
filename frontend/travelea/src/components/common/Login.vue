<template>
    <v-form>
      <v-container>
        <v-layout align-center justify-center row fill-height/>
                <v-flex xs12 sm6 md3>
                    <v-text-field
                    label="Email"
                    ></v-text-field>
                </v-flex>

                <v-flex xs12 sm6 md3>
                    <v-text-field
                    label="Password"
                    ></v-text-field>
                </v-flex>
        
                <v-btn round color="primary">Login</v-btn v-on:click.login>
        </v-layout>
      </v-container>
    </v-form>
</template>


<script>

export default