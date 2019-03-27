const rules = {
  required: [v => v != "" || "Required", v => v != undefined || "Required"],
};

export default rules;
