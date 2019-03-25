import { getImages } from "../../repository/PersonalPhotosRepository";
import AuthRepository from "../../repository/AuthRepository";

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
        state.id = id;
      },
      setToken(state, token) {
        state.token = token;
      },
      setProfile(state, profile) {
        state.profile = profile;
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
      }
    },
    getters: {
      getUser: state => state,      
    }
}