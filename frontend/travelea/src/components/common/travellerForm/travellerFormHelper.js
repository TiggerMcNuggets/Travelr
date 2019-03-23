let travellerFormHelper = {};
/**
 * Returns a nationality list as a list of objects with id and hasPassport field
 * @param natList list of nationalities with id field
 * @param passList list of passports with id field
 * @return the new list of nationalities
 */
travellerFormHelper.convertToNationalitiesReq = (natList, passList) => {
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

/**
 * Takes in a list of nationalities with passport field and 
 * returns an array of nationalities and an array of passports 
 * @param natList The list of nationalities with hasPassport field
 */
travellerFormHelper.convertFromNationalitiesRes = (natList) => {
  let nationalities = [];
  let passports = [];
  for (let nat of natList) {
    nationalities.push({id: nat.id, name: nat.name});
    if (nat.hasPassport) {
      passports.push({id: nat.id, name: nat.name});
    }
  }
  return [nationalities, passports];
}



export default travellerFormHelper;
