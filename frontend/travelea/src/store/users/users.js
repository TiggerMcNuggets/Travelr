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
            console.log('###############################################################');
            console.log(response);
            console.log('###############################################################');
            commit('setUsers', response);
          } catch (e) {
            return;
          }
        },
    },
    getters: {
        users(state) {
            return state.users;
        },
        nationalities(state) {
            return state.nationalities;
        },
        travellers(state) {
            return state.travellerTypes;
        }
    }
}