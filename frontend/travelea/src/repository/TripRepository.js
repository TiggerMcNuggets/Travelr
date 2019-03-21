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