let signup = {};
/**
 * Returns a nationality list as a list of objects with id and hasPassport field
 * @return the new list of nationalities
 */
signup.nationalitiesAsObject = (natList, passList) => {
  let nationalities = [];
  for (let nat of natList) {
    let nation = { id: nat.id, hasPassport: false };
    if (passList.includes(nat.id)) {
      nation.hasPassport = true;
    }
    nationalities.push(nation);
  }
  return nationalities;
};

export default signup;
