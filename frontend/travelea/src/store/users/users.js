import { getAllUsers } from "../../repository/UserRepository";
import { getAllNationalities } from "../../repository/UserRepository";
import { getAllTravellerTypes } from "../../repository/UserRepository";
import UserRepository from "../../repository/UserRepository";

/**
 * Users store sub-module
 */
export default {
    state: {
        users: [],
        nationalities: [],
        travellerTypes: []
    },
    mutations: {
        setUsers(state, users) {
            //const users = await getAllUsers();
            state.users = users;
        },
        async setNationalities(state) {
            const nationalities = await getAllNationalities();
            state.nationalities = nationalities;
        },
        async setTravellers(state) {
            const travellerTypes = await getAllTravellerTypes();
            state.travellerTypes = travellerTypes;
        }
    },
    actions: {
        async getUsers({commit}, params) {
          try {
            const response = await UserRepository.getUsers(params);
            console.log(response['data']);
            commit('setUsers', response['data']);
          } catch (e) {
            return;
          }
        },
    },
    getters: {
        users: (state) => state.users,
        nationalities: (state) => state.nationalities,
        travellers: (state) => state.travellerTypes
    }
}