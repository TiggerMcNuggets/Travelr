/**
 * retrieves the list of all the destinations stored in the database 
 */
export const getAllUsers = async() => {

    let url = "http://localhost:9000/travellers";

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