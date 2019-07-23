<template>
  <v-container grid-list-md fluid ma-0 pa-0>
    <v-layout row wrap>
      <v-flex md12>
        <v-text-field
          label="Email"
          :value="email"
          :rules="rules.emailCheck"
          @input="$emit('update:email', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex md12>
        <v-text-field
          label="Password"
          type="password"
          :value="password"
          :rules="rules.required"
          @input="$emit('update:password', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex md12>
        <v-text-field
          label="Confirm Password"
          type="password"
          :value="confirmPassword"
          :rules="rules.confirmPassCheck"
          @input="$emit('update:confirmPassword', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
</style>

<script>
import rules from "../common/formRules";
export default {
  name: "SignupFields",

  props: ["email", "password", "confirmPassword"],

  data() {
    return { rules };
  },

  /**
   * Adds form rules when component has mounted.
   */
  mounted() {
    this.addConfirmPassRule();
    this.addEmailRule();
  },

  methods: {
    /**
     * Adds a comfirm password rule to the second password field in the form.
     */
    addConfirmPassRule() {
      this.$set(
        this.rules,
        "confirmPassCheck",
        this.rules.required.concat([
          () => this.password === this.confirmPassword || "Passwords must match"
        ])
      );
    },

    /**
     * Adds an email validation rule to the email field.
     */
    addEmailRule() {
      const emailPattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      this.$set(
        this.rules,
        "emailCheck",
        this.rules.required.concat([
          () => emailPattern.test(this.email) || "Must be in email format"
        ])
      );
    }
  }
};
</script>
