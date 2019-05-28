import Repository from "./Repository";

export default {
    attemptLogin(payload) {
        return Repository.post('/login', payload);
    }
}