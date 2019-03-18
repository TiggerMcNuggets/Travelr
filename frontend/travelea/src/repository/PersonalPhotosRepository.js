export const storeImage = async (id, data) => {

    let url = "http://localhost:9000/travellers/" + id + "/photo";
    console.log(url);
    console.log(data)

    // const result = await fetch(url, {
    //     method: "POST", 
    //     headers: {
    //         'Content-Type': 'multipart/form-data'
    //     },
    //     body: data 
    // })
    //     .then(response => {
    //         return response;
    //     });

    // return result;

    // var formData  = new FormData();

    // for(var name in data) {
    //   formData.append(name, data[name]);
    // }
  
    const result = await fetch(url, {
      method: 'POST',
      body: data
    }).then(function (response) {
        return response
    });

    return result;
}