import Repository from "./Repository";


export default {

    // Gets all user nationalities
    get() {
        return Repository.get('/nationalities')
    }
}


