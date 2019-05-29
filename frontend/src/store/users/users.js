import UserRepository from "../../repository/UserRepository";
import dateTime from "../../components/common/dateTime/dateTime.js";

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
            state.users = users;
        },
        setNationalities(state, nationalities) {
            state.nationalities = nationalities;
        },
        setTravellerTypes(state, travellerTypes) {
            state.travellerTypes = travellerTypes;
        }
    },
    actions: {
        async getUsers({ commit }, params) {
            //Creating new params so the original params sent through don't get edited
            let new_params = {};
            if (params) {
                if (params.firstName !== '') {
                    new_params.firstName = params.firstName;
                }
                if (params.lastName !== '') {
                    new_params.lastName = params.lastName;
                }
                if (params.gender !== '') {
                    new_params.gender = params.gender;
                }
                if (params.minAge !== '') {
                    new_params.minAge = params.minAge;
                }
                if (params.maxAge !== '') {
                    new_params.maxAge = params.maxAge;
                }
                if (params.nationality.length !== 0) {
                    new_params.nationality = params.nationality;
                }
                if (params.travellerType.length !== 0) {
                    new_params.travellerType = params.travellerType;
                }
                if (params.orderBy !== '') {
                    new_params.orderBy = params.orderBy;
                }
            }
            try {
                const response = await UserRepository.getUsers(new_params);
                let filtered_data = [];
                for (let i = 0; i < response['data'].length; i++) {
                    response.data[i]['dateOfBirth'] = dateTime.convertTimestampToString(response.data[i]['dateOfBirth']);
                    let meets_nationality_param = true;
                    let meets_traveller_param = true;
                    if (new_params.nationality) {
                        meets_nationality_param = false;
                        for (let l = 0; l < response['data'][i]['nationalities'].length; l++) {
                            if (new_params.nationality.includes(response['data'][i]['nationalities'][l]['name'])) {
                                meets_nationality_param = true;
                            }
                        }
                    }
                    if (new_params.travellerType) {
                        meets_traveller_param = false;
                        for (let l = 0; l < response['data'][i]['travellerTypes'].length; l++) {
                            if (new_params.travellerType.includes(response['data'][i]['travellerTypes'][l]['name'])) {
                                meets_traveller_param = true;
                            }
                        }
                    }
                    if (meets_nationality_param && meets_traveller_param) {
                        filtered_data.push(response['data'][i]);
                    }
                }
                if (new_params.orderBy) {
                    if (new_params.orderBy === 'Nationality') {
                        filtered_data.sort((a, b) => (a.nationalities[0] > b.nationalities[0]) ? -1 : 1);
                    } else {
                        filtered_data.sort((a, b) => (a.travellerTypes[0] > b.travellerTypes[0]) ? -1 : 1);
                    }
                }
                commit('setUsers', filtered_data);
            } catch (e) {
                console.error(e);
            }
        },
        async getAllTravellerTypes({ commit }) {
            try {
                const response = await UserRepository.getTravellerTypes();
                let types = [];
                //Go throught each object and return only a list of strings
                for (let i = 0; i < response['data'].length; i++) {
                    types.push(response['data'][i]['name']);
                }
                types.sort();
                commit('setTravellerTypes', types);
            } catch (e) {
                console.log(e);
            }
        },
        async getAllNationalities({ commit }) {
            try {
                const response = await UserRepository.getNationalities();
                let nationalities = [];
                //Go through each object and return only a list of strings
                for (let i = 0; i < response['data'].length; i++) {
                    nationalities.push(response['data'][i]['name']);
                }
                nationalities.sort();
                commit('setNationalities', nationalities);
            } catch (e) {
                console.log(e);
            }
        },

        // eslint-disable-next-line
        async toggleUserDeleted({commit}, userId) {
            await UserRepository.toggleUserDeleted(userId);
        }
    }
}