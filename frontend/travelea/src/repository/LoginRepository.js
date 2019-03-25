
export const attemptLogin = async (data) => {

    let url = "http://localhost:9000/login";

    const result = await fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(
            response => {
                console.log(response);
                if (response.ok) {
                    return response.json();
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
};