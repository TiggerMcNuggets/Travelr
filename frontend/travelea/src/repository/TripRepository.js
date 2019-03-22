export const createTrip = async (tripBody) => {

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
export const getTrips = async (id) => {

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

export const deleteTripById = async (id) => {
    let url = "http://localhost:9000/trips/" + id;
    const result = await fetch(url, {
        method: "DELETE", // *GET, POST, PUT, DELETE, etc.
    })
        .then(response => {
            return response;
        });

    return result;

}