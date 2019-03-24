<template>
    <v-form ref="form" v-model="isValid" lazy-validation>
        <v-flex lg6 offset-lg3 sm8 offset-sm2 text-xs-center>
            <v-card class="signup-card">

                <TravellerForm
                        :fname.sync="traveller.firstName"
                        :mname.sync="traveller.middleName"
                        :lname.sync="traveller.lastName"
                        :dob.sync="dateOfBirth"
                        :gender.sync="traveller.gender"
                        :types.sync="traveller.travellerTypes"
                        :nationalities.sync="nationalities"
                        :passports.sync="passports" />

                <SignupFields
                        :email.sync="traveller.email"
                        :password.sync="traveller.password"
                        :confirmPassword.sync="confirmPassword" />

                <v-layout wrap>
                        <v-flex xs12 sm6 md8 class="margin-left-to-checkbox">
                            <v-checkbox
                                    v-model="checkbox"
                                    :label="'Create user as admin'"
                            >
                            </v-checkbox>
                        </v-flex >
                        <v-flex xs12 sm6 md3>
                            <v-btn :disabled="!isValid" color="primary" @click="handleSignup">
                                Create Profile
                            </v-btn>
                        </v-flex>
                </v-layout>
            </v-card>
            <v-alert class="email-alert" :value="emailAlert" color="error">Email already taken</v-alert>
            <v-alert class="success" :value="createdUser" color="success">User created!</v-alert>
        </v-flex>
    </v-form>
</template>

<style>
    .signup-card {
        margin-top: 20px;
    }
    .email-alert {
        display: block;
        margin-top: 10px;
    }
    .margin-left-to-checkbox {
        margin-left: 2em
    }
</style>

<script>
    import TravellerForm from "../common/travellerForm/TravellerForm";
    import SignupFields from "../signup/SignupFields";

    import signup from "../signup/signup.js";
    import dateTime from "../common/dateTime/dateTime.js";

    import {store} from "../../store/index";

    export default {
        name: "AdminCreateProfile",
        components: { TravellerForm, SignupFields },
        store,
        data() {
            return {
                checkbox: true,
                isValid: false,
                emailAlert: false,
                createdUser: false,
                traveller: {},

                dateOfBirth: "",
                nationalities: [],
                passports: [],
                confirmPassword: "",
            };
        },
        methods: {
            setTraveller() {
                this.$set(this.traveller, "nationalities", signup.nationalitiesAsObject(this.nationalities, this.passports));
                this.$set(this.traveller, "dateOfBirth", dateTime.convertDate(this.dateOfBirth));
                if (this.checkbox) {
                    this.$set(this.traveller, "accountType", 1);
                }
            },

            async signup() {
                const response = await store.dispatch("signupOtherUser", this.traveller);

                if (!response) {
                    this.emailAlert = true;
                    return false;
                }
                return true;

            },
            async handleSignup() {
                if (this.$refs.form.validate()) {
                    this.setTraveller();

                    if (await this.signup()) {
                        this.createdUser = true;
                        setTimeout(() => {
                            this.createdUser = false
                        }, 3000);
                    }
                }
            },
        },
    };
</script>
