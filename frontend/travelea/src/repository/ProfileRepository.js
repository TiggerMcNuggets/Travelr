import Repository from "./Repository";


export default {
    getProfile(id) {
        let data = {
            "profile": {
                "firstName": "Adam",
                "lastName": "Conway",
                "date_of_birth": "17-08-1998",
                "nationality": [
                    "New Zealander",
                    "Australian"
                ],
                "travellerType": [
                    "Backpacker",
                    "Explorer"
                ],
                "passportCountries": [
                    "New Zealand"
                ]
            },
            "trips": [],
            "photos": []            
        }

    return data;   
    
    }
}
