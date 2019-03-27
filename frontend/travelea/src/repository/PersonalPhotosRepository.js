import Repository from "./Repository";


// Basic fetch POST function to upload the image to the server.
export const storeImage = async (id, data) => {

    // let url = "http://localhost:9000/api/travellers/" + id + "/photo";
    
    // const result = await fetch(url, {
    //   method: 'POST',
    //   body: data,
    //   headers:{
    //     'X-Authorization': "123"
    //   }
    // }).then(function (response) {
    //     return response
    // });

    // return result;
    console.log('storing the photo')
    return Repository.post(`/travellers/${id}/photo`, data);
}

// Gets the image file names from the serer associated with  a traveller using fetch.
export const getImages = async (id) => {

    // let url = "http://localhost:9000/api/travellers/" + id + "/photo";
    
    // const result = await fetch(url, {headers:{
    //     'X-Authorization': "123"
    //   }})
    //     .then(
    //         response => {
    //             console.log(response);
    //             if (response.ok) {
    //                 return response.json();
    //             }
    //             throw new Error("Request Failed");
    //         },
    //         networkError => {
    //             console.log(networkError.message);
    //         }
    //     ).then(jsonResponse => {
    //         console.log(jsonResponse)
    //         return jsonResponse
    //     });
    

    // return result;
    console.log('getting the photo')
    return Repository.get(`/travellers/${id}/photo`);
}

export const updatePersonalPhoto = async (payload) => {
    console.log('updating the photo')
    return Repository.post(`/travellers/photo/${payload.id}`, payload);
}   