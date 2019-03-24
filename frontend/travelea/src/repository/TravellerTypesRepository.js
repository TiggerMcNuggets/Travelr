import Repository from "./Repository";


export default {
    get() {
        return Repository.get('/traveller-types')
    }
}


