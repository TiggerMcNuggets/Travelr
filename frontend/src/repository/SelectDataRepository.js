import Repository from "./Repository";

export default {

  // Gets all nationalities (DUPLICATE see Nationality Repository)
  nationalities() {
    return Repository.get("/nationalities");
  },

  // Gets all traveller types (DUPLICATE see Traveller Types Repository)
  travellerTypes() {
    return Repository.get("/traveller-types");
  },
};
 