import Repository from "./Repository";


export default {
    getUsers(params) {
        return Repository.get('/travellers', {
            params: params
        });
    },

    getUser(id) {
        return Repository.get(`/travellers/${id}`);
    },

    createUser(payload) {
        return Repository.post('/travellers', payload); 
    },

    updateUser(id, payload) {
        return Repository.put(`/travellers/${id}`, payload);
    }


}



/**
 * retrieves the list of all the users stored in the database which meet the query paramaters
 */
export const getAllUsers = async(search_params = false) => {

    let url = "http://localhost:9000/travellers";

    if (search_params !== false) { //Params have been suplied in an object, will go through to check each param and build the url query
        url = url + '?';
        if (search_params.fName !== '') {
            url = url + 'fname=' + search_params.fName + '&'
        }
        if (search_params.lName !== '') {
            url = url + 'lastName=' + search_params.lName + '&'
        }
        if (search_params.minAge !== '') {
            url = url + 'minAge=' + search_params.minAge + '&'
        }
        if (search_params.maxAge !== '') {
            url = url + 'maxAge=' + search_params.maxAge + '&'
        }
        if (search_params.gender !== '') {
            if (search_params.gender === 'Female') {
                url = url + 'gender=f&'
            } else {
                url = url + 'gender=m&'
            }
        }
        if (search_params.nationality !== '') {
            url = url + 'nationality=' + search_params.nationality + '&'
        }
        if (search_params.travellerTypes !== '' && search_params.travellerTypes !== []) {
            url = url + 'travellerType=' + search_params.travellerTypes + '&'
        }
        if (search_params.orderBy !== '') {
            url = url + 'orderBy=' + search_params.orderBy + '&'
        }
    }
    const result = await fetch(url, {
            method: "GET",
            headers: {
                "X-Authorization": "5e47683d-264f-4a96-a380-290b9e2c4c7c" //NEED TO FIGURE OUT HOW TO GET THIS PROPERLY
            }
        })
        .then(
            response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error("Request Failed");
            },
            networkError => {
                console.log(networkError.message);
            }
        ).then(jsonResponse => {
            console.log(jsonResponse)
            return jsonResponse
        });
    return result;
    //Go through each user from database and correct fields
    /*for (let i = 0; i < result.length; i++) {
        let correct_date = new Date(result[i]['dob']).toLocaleDateString();
        let gender = 'Female';
        if (result[i]['gender'] === 'm') {
            gender = 'Male';
        }
        result[i]['dob'] = correct_date;
        result[i]['gender'] = gender;
    }
    //return result;
    let return_result = [];
    let i = 0;
    for (; i < 20; i++) {
        let gen = "Male";
        if (i % 2 == 0) {
            gen == "Female";
        }
        return_result.push({
            "id": i,
            "firstName": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
            "middleName": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
            "lastName": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
            "dateOfBirth": 0,
            "gender": gen,
            "nationalities": [{
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
                    "hasPassport": 0
                },
                {
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
                    "hasPassport": 0
                },
                {
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5),
                    "hasPassport": 0
                }
            ],
            "travellerTypes": [{
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5)
                },
                {
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5)
                },
                {
                    "id": 0,
                    "name": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5)
                }
            ]
        })
    }
    console.log('##############################################################################');
    console.log(return_result);
    return return_result*/
};

/**
 * retrieves the list of all the nationalities stored in the database
 */
export const getAllNationalities = async() => {

    let url = "http://localhost:9000/nationalities";

    const result = await fetch(url, {
            method: "GET",
            headers: {
                "X-Authorization": "5e47683d-264f-4a96-a380-290b9e2c4c7c" //NEED TO FIGURE OUT HOW TO GET THIS PROPERLY
            }
        })
        .then(
            response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error("Request Failed");
            },
            networkError => {
                console.log(networkError.message);
            }
        ).then(jsonResponse => {
            console.log(jsonResponse);
            return jsonResponse
        });

    let nationalities = [];
    //Go throught each object and return only a list of strings
    for (let i = 0; i < result.length; i++) {
        nationalities.push(result[i]['name']);
    }
    nationalities.sort();
    return nationalities;
    //return [3, 2, 1]
};

/**
 * retrieves the list of all the traveller types stored in the database
 */
export const getAllTravellerTypes = async() => {
    let url = "http://localhost:9000/traveller-types";
    const result = await fetch(url, {
            method: "GET",
            headers: {
                "X-Authorization": "5e47683d-264f-4a96-a380-290b9e2c4c7c" //NEED TO FIGURE OUT HOW TO GET THIS PROPERLY
            }
        })
        .then(
            response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error("Request Failed");
            },
            networkError => {
                console.log(networkError.message);
            }
        ).then(jsonResponse => {
            console.log(jsonResponse)
            return jsonResponse
        });
    let travellerTypes = [];
    //Go throught each object and return only a list of strings
    for (let i = 0; i < result.length; i++) {
        travellerTypes.push(result[i]['name']);
    }
    travellerTypes.sort();
    return travellerTypes;
    //return[1, 2, 3, 4];
};