import { getAllUsers } from "../../repository/UserRepository";

/**
 * Users store sub-module
 */
export default {
    state: {
        users: []
    },
    mutations: {
        async setUsers(state) {
            const users = await getAllUsers();
            state.users = users;
        },
    },
    actions: {

    },
    getters: {
        users(state) {
            return state.users;
        }
    }
}