import Repository from "./Repository";

export default {

    // Attempts a user login
    attemptLogin(payload) {
        return Repository.post('/login', payload);
    }
}