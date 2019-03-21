import { getImages } from "../../repository/PersonalPhotosRepository";

export default {
    state: {
        user: null
    },
    mutations: {
        async setPersonalImages(state, id) {
            const personalPhotos = await getImages(id);
            state.personalPhotos = personalPhotos;
        }
    },
    actions: {

    },
    getters: {
        user(state) {
            return state.user;
        }
    }
}