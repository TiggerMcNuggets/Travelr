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
      if (!nat.old) {
          let nation = {id: nat.id, hasPassport: false};
          if (passList.includes(nat.id)) {
              nation.hasPassport = true;
          }
          nationalities.push(nation);
      }
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
    nationalities.push({id: nat.id, name: nat.name, old: nat.old});
    if (nat.hasPassport) {
      passports.push({id: nat.id, name: nat.name});
    }
  }
  return [nationalities, passports];
};

/**
 * Takes in a list of traveller type objects and returns a list of ids numbers
 * @param travellerTypeList list of traveller type objects 
 * @return array of numbers (the id)
 */
travellerFormHelper.convertFromTravellerTypesRes = (travellerTypeList) => {
  let typesList = [];
  for (let types of travellerTypeList) {
    typesList.push(types.id);
  }
  return typesList;
}



export default travellerFormHelper;
