import Repository from "./Repository";


export default {

    // Gets all traveller types.
    get() {
        return Repository.get('/traveller-types')
    }
}


