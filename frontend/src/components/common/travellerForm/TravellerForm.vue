<template>
  <v-container grid-list-md fluid ma-0 pa-0>
    <v-layout row wrap>
      <v-flex sm6 xs12>
        <v-text-field
          label="First Name"
          :value="fname"
          :rules="rules.required"
          @input="$emit('update:fname', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
      <v-flex sm6 xs12>
        <v-text-field
          label="Middle Name"
          :value="mname"
          @input="$emit('update:mname', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex sm6 xs12>
        <v-text-field
          label="Last Name"
          :value="lname"
          :rules="rules.required"
          @input="$emit('update:lname', $event)"
          maxlength="50"
        ></v-text-field>
      </v-flex>
      <v-flex sm6 xs12>
        <Datepicker v-on:updateDob="updateDob" :dob="dob"/>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex sm6 xs12>
        <v-select
          label="Gender"
          :items="genderList"
          :value="gender"
          :rules="rules.required"
          @input="$emit('update:gender', $event)"
        ></v-select>
      </v-flex>
      <v-flex sm6 xs12>
        <v-select
          label="Traveller Types"
          :items="typeList"
          item-text="name"
          item-value="id"
          :value="types"
          :rules="rules.required"
          @input="$emit('update:types', $event)"
          attach
          multiple
        ></v-select>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex sm6 xs12>
        <v-select
          label="Nationality"
          :items="nationalityList"
          item-text="name"
          :returnObject="true"
          :value="nationalities"
          :rules="rules.required"
          @input="$emit('update:nationalities', $event)"
          attach
          multiple
        ></v-select>
      </v-flex>
      <v-flex sm6 xs12>
        <v-select
          label="Passport"
          :items="nationalities"
          item-text="name"
          item-value="id"
          :value="passports"
          @input="$emit('update:passports', $event)"
          attach
          multiple
        ></v-select>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
</style>

<script>
import Datepicker from "../dateTime/Datepicker";
import SelectDataRepository from "../../../repository/SelectDataRepository";
import rules from "../formRules";
export default {
  name: "TravellerForm",
  components: { Datepicker },
  props: [
    "fname",
    "mname",
    "lname",
    "dob",
    "gender",
    "types",
    "nationalities",
    "passports"
  ],
  data() {
    return {
      rules,
      //Select lists
      genderList: ["Male", "Female", "Other"],
      typeList: [],
      nationalityList: []
    };
  },
  mounted() {
    this.populateSelects();
  },

  methods: {

    /**
     * Populates the selection form items such as traveller types and nationalities.
     */
    async populateSelects() {
      const nationalities = await SelectDataRepository.nationalities();
      this.nationalityList = nationalities.data;
      const travellerTypes = await SelectDataRepository.travellerTypes();
      this.typeList = travellerTypes.data;
    },

    /**
     * Updates the dat ef birth element with a given value.
     */
    updateDob(val) {
      this.$emit("update:dob", val);
    }
  }
};
</script>
