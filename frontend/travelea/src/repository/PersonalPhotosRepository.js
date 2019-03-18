
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