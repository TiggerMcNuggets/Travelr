import { getImages } from "../../repository/PersonalPhotosRepository";
import AuthRepository from "../../repository/AuthRepository";
import ProfileRepository from "../../repository/ProfileRepository";
import UserRepository from "../../repository/UserRepository";

const emptyProfile = {
    firstName: "",
    middleName: "",
    lastName: "",
    dateOfBirth: null,
    gender: "",
    nationalities: [],
    travellerTypes: [],
    email: null,
    accountType: 0
};

export default {
    state: {
        user: null,
        token: localStorage.getItem("token")
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
                try {
                    const response = await AuthRepository.login(loginData);
                    commit('setToken', response.data.token);
                    commit('setUser', response.data.user);
                    localStorage.setItem("token", response.data.token)
                    resolve()
                } catch (e) {
                    commit('setToken', "");
                    commit('setUser', null);
                    reject(e);
                }
            })
        },

        signup({ commit }, signupData) {
            return new Promise((resolve, reject) => {
                try {
                    const response = await AuthRepository.signup(signupData);
                    commit('setId', response.data.id);
                } catch (e) {
                    reject(e);
                }
            })

        },
        updateUser({ commit }, editData) {
            return new Promise((resolve, reject) => {
                try {
                    const response = await ProfileRepository.editProfile(editData, state.user.id);
                    user
                } catch (e) {
                    reject(e)
                }
            })

            commit('setProfile', editData.data);
        },
        fetchMe({ commit }) {
            return new Promise((resolve, reject) => {
                try {
                    const response = await UserRepository.getMe();
                    commit('setUser', response.data);
                } catch (e) {
                    localStorage.removeItem("token");
                    commit('logout');
                    reject(e);
                }
            })
        },

        logout({ commit }) {
            return new Promise((resolve, reject) => {
                try {
                    const response = await AuthRepository.logout();
                    commit('logout')
                    localStorage.removeItem("token")
                } catch (e) {
                    commit('logout')
                    localStorage.removeItem("token")
                    reject(e);
                }
            })
        }

    },
    getters: {
        getUser: state => state.user,
        getToken: state => state.token,
        getIsUserAdmin: state => (state.user.profile.accountType > 0)
    }

}