import moment from "moment";
// set of rules

export const rules = {
  nameRules: [
    v => !!v || "This field is required",
    v => (v && v.length <= 20) || "This field is too long"
  ],
  numberRules: [
    v => !!v || "This field is required",
    v => (v && (isInt(v) || isFloat(v))) || "This field must be a numeric value"
  ]
};

// rules helpers (depending on how big they become we can move this into helpers)

export const noSameDestinationNameConsecutiveRule = destinations => {
  let noConsecutiveSame = true;
  for (let i = 0; i < destinations.length; i++) {
    if (i + 1 < destinations.length) {
      if (destinations[i].title === destinations[i + 1].title) {
        noConsecutiveSame = false;
      }
    }
    if (i - 1 >= 0) {
      if (destinations[i].title === destinations[i - 1].title) {
        noConsecutiveSame = false;
      }
    }
  }
  return [noConsecutiveSame || "Cannot have same destination consecutive"];
};

export const arrivalBeforeDepartureAndDestinationsOneAfterTheOther = destinations => {
  let noArrivalAfterDeparture = true;
  for (let i = 0; i < destinations.length; i++) {
    if (
      moment(destinations[i].arrivalDate).isAfter(
        moment(destinations[i].departureDate)
      )
    ) {
        noArrivalAfterDeparture = false;
    }
  }
  return [
    noArrivalAfterDeparture || "Arrival time must be before departure",
  ];
};

function isInt(n) {
  return Number(n) === n && n % 1 === 0;
}

function isFloat(n) {
  return Number(n) === n && n % 1 !== 0;
}
