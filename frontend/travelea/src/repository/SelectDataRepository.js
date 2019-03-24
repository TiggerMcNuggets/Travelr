import Repository from "./Repository";

export default {
  nationalities() {
    return Repository.get("/nationalities");
  },
  travellerTypes() {
    return Repository.get("/traveller-types");
  },
};
 