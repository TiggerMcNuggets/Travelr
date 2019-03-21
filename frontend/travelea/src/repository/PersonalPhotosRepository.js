
// Basic fetch POST function to upload the image to the server.
export const storeImage = async (id, data) => {

    let url = "http://localhost:9000/travellers/" + id + "/photo";
    
    const result = await fetch(url, {
      method: 'POST',
      body: data
    }).then(function (response) {
        return response
    });

    return result;
}

// Basic fetch POST function to upload the image to the server.
export const getImages = async (id) => {

    let url = "http://localhost:9000/travellers/" + id + "/photo";
    
    const result = await fetch(url)
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
}

// Basic fetch POST function to upload the image to the server.
export const getImage = async (filename) => {

    let url = "http://localhost:9000/travellers/photo" + filename;
    
    const result = await fetch(url)
        .then(
            response => {
                console.log(response);
                if (response.ok) {
                    return response
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