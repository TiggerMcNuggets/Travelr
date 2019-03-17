import * as _ from "lodash";

export default {
  nameRules: [
    v => !!v || "This field is required",
    v => (v && v.length <= 20) || "This field is too long"
  ],
  numberRules: [
    v => !!v || "This field is required",
    v => (v && _.isNumber(v)) || "This field must be a numeric value"
  ]
};