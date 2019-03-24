import { getImages } from "../../repository/PersonalPhotosRepository";
import AuthRepository from "../../repository/AuthRepository";
import ProfileRepository from "../../repository/ProfileRepository";

export default {
    state: {
        user: {
          id: null,
          token: "",
          profile: {
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
        }
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

      async fetchUser({commit}, id) {
        const response = await ProfileRepository.getProfile(id);
        commit('setProfile', response.data);
      },

      async updateUser({commit}, editData) {
        await ProfileRepository.editProfile(editData);
        commit('setProfile', editData.data);
      },
  
    },
    getters: {
      getUser: state => state.user,
      isLoggedIn: state => (state.user.id !== null && state.user.token !== ""),
    }
}