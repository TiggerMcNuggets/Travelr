import AuthRepository from "../../repository/AuthRepository";
// import ProfileRepository from "../../repository/ProfileRepository";
import UserRepository from "../../repository/UserRepository";


export default {
    state: {
        user: null,
        token: localStorage.getItem("token") || ""
    },

    mutations: {
        setToken(state, token) {
            state.token = token;
        },
        setUser(state, user) {
            state.user = user;
        },
        logout(state) {
            state.user = null;
            state.token = "";
        }
    },

    actions: {
        login({ commit }, loginData) {
            return new Promise((resolve, reject) => {
                AuthRepository.login(loginData)
                    .then(response => {
                        commit('setToken', response.data.token);
                        commit('setUser', response.data.user);
                        localStorage.setItem("token", response.data.token);
                        resolve(response);
                    })
                    .catch(err => {
                        commit('setToken', "");
                        commit('setUser', null);
                        reject(err);
                    })
            })
        },

        signup(signupData) {
            return new Promise((resolve, reject) => {
                AuthRepository.signup(signupData)
                .then(() => {
                    resolve();
                })
                .catch(err => {
                    reject(err)
                })         
            })
        },
        // updateUser({ commit }, editData) {
        //     return new Promise((resolve, reject) => {
        //         ProfileRepository.editProfile(editData, state.user.user.id)
        //         .then(resp => {
        //             // UPDATE USER

        //         })
        //         .catch(err => {
        //             reject(err)
        //         })
        //     })            
        // },
        fetchMe({ commit }) {
            return new Promise((resolve, reject) => {
                UserRepository.getMe()
                    .then(response => {
                        commit('setUser', response.data);
                        resolve(response);
                    })
                    .catch(err => {
                        localStorage.removeItem("token");
                        commit('logout');
                        reject(err);
                    })
            })
        },

        logout({ commit }) {
            return new Promise((resolve, reject) => {
                AuthRepository.logout()
                    .then(() => {
                        commit('logout')
                        localStorage.removeItem("token")
                        resolve();
                    })
                    .catch(err => {
                        commit('logout')
                        localStorage.removeItem("token")
                        reject(err);
                    })
            })
        }
    },
    getters: {
        getUser: state => state.user,
        getToken: state => state.token,
        getIsUserAdmin: state => (state.user && state.user.accountType > 0),
        isLoggedIn: state => (state.user)
    }

}