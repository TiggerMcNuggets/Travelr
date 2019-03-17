/**
 * retrieves the list of all the destinations stored in the database 
 */
export const getDestination = async () => {

    let url = "http://localhost:9000/destinations";

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

export const getOneDestination = async (id) => {

    let url = "http://localhost:9000/destinations/" + id;
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

export const updateDestination = async (id, data) => {

    let url = "http://localhost:9000/destinations/" + id;

    const result = await fetch(url, {
        method: "PUT", // *GET, POST, PUT, DELETE, etc.
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => {
            return response;
        });

    return result;

}

export const createDestination = async (data) => {

    let url = "http://localhost:9000/destinations";

    const result = await fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => {
            return response;
        });

    return result;

}

export const deleteDestination = async (id) => {

    let url = "http://localhost:9000/destinations/" + id;
    const result = await fetch(url, {
        method: "DELETE", // *GET, POST, PUT, DELETE, etc.
    })
        .then(response => {
            return response;
        });

    return result;

}

