
export const attemptLogin = async (data) => {

    let url = "http://localhost:9000/login";

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