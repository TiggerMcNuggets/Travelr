import { getImages } from "../../repository/PersonalPhotosRepository";
import AuthRepository from "../../repository/AuthRepository";
import ProfileRepository from "../../repository/ProfileRepository";

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
}

export default {
    state: {
        user: {
          id: null,
          token: "",
          profile: emptyProfile,
        },
    },

    mutations: {
      setId(state, id) {
        state.user.id = id;
      },
      setToken(state, token) {
        state.user.token = token;
      },
      setProfile(state, profile) {
        state.user.profile = profile;
      }
    },

    actions: {
      async login({commit}, loginData) {
        try {
          const response = await AuthRepository.login(loginData);
          commit('setToken', response.data.token);
        } catch (e) {
          return;
        }
      },

      async signup({commit}, signupData) {
        try {
          const response = await AuthRepository.signup(signupData);
          commit('setId', response.data.id);
        } catch (e) {
          return;
        }
      },

      async logout({commit}) {
        const response = await AuthRepository.logout();
        commit('setId', null);
        commit('setToken', "");
        commit('setProfile', emptyProfile);
      },

      async fetchUser({commit}, id) {
        const response = await ProfileRepository.getProfile(id);
        commit('setProfile', response.data);
      },

      async fetchMe({commit}, id) {
        const response = await ProfileRepository.getMe();
        commit('setId', response.data.id);
        commit('setToken', localStorage.getItem('token'));
        commit('setProfile', response.data);
      },

      async updateUser({commit}, editData, id) {
        await ProfileRepository.editProfile(editData, id);
        commit('setProfile', editData.data);
      },
  
    },
    getters: {
      getUser: state => state.user,
      getId: state => state.user.id,
      getToken: state => state.user.token,
      isLoggedIn: state => {return (state.user.id !== null && state.user.token !== "")}
    }
}