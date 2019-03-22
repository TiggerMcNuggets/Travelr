export const createTrip = async(tripBody) => {

    let url = "http://localhost:9000/trips";

    return await fetch(url, {
        method: "POST",
        headers: {
            // TODO: wait until authentication is in place
            "Content-Type": "application/json",
            // "X-Authorization": "/////must retrieve this from the store/////"
        },
        body: JSON.stringify(tripBody),
    }).then(response => {
        return response;
    });
};

/**
 * retrieves the list of all the destinations stored in the database 
 */
export const getTrips = async(id) => {

    // TODO: swap when backend is authenticated
    // let url = `http://localhost:9000/${id}/trips`;
    let url = `http://localhost:9000/trips`;

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
            console.log(jsonResponse);
            return jsonResponse
        });

    return result;
};

export const deleteTripById = async(id) => {
    let url = "http://localhost:9000/trips/" + id;
    const result = await fetch(url, {
            method: "DELETE", // *GET, POST, PUT, DELETE, etc.
        })
        .then(response => {
            return response;
        });

    return result;

}

export const getTripWithId = async(id) => {
    console.log(id);
    //let url = "http://localhost:9000/trips/" + id;
    let url = "http://localhost:9000/trips"; //used until API updated
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
            console.log(jsonResponse);
            return jsonResponse
        });
    let trip = {};
    let i = 0;
    for (i = 0; i < result.length; i++) {
        if (result[i]['id'] == id) {
            trip = result[i];
        }
    }
    /*let ordered_dests = result.destinations.sort(function(a, b){
        return a.ordinal - b.ordinal;
    });
    result.destinations = ordered_dests;*/ //Will be needed later for when we're not sure if destinations are in the right order
    //return trip;
    //return result;
    let to_return = {
        "id": 0,
        "name": "string",
        "destinations": [{
                "id": 0,
                "name": "string",
                "ordinal": 0,
                "arrivalDate": 0,
                "departureDate": 0
            },
            {
                "id": 0,
                "name": "string",
                "ordinal": 0,
                "arrivalDate": 0,
                "departureDate": 0
            },
            {
                "id": 0,
                "name": "string",
                "ordinal": 0,
                "arrivalDate": 0,
                "departureDate": 0
            }
        ]
    }
    return to_return;
}