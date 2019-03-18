/**
 * retrieves the list of all the destinations stored in the database 
 */
export const getAllUsers = async(search_params = false) => {
    let url = "http://localhost:9000/travellers";
    if (search_params !== false) {
        url = url + '?';
        //console.log(search_params)
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
    //console.log(url);
    const result = await fetch(url)
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
}