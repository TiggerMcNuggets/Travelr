
export default class Destination {

    static async getDestination() {

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

    static async getOneDestination(id) {

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

    static async updateDestination(id, data) {

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

    static async createDestination(data) {

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

    static async deleteDestination(id) {

        let url = "http://localhost:9000/destinations/" + id;
        const result = await fetch(url, {
            method: "DELETE", // *GET, POST, PUT, DELETE, etc.
        })
            .then(response => {
                return response;
            });

        return result;

    }
}


