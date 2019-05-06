import Repository from "./Repository";


// Basic fetch POST function to upload the image to the server.
export const storeDestinationImage = async (id, data) => {
    return Repository.post(`/users/${id}/destinations/${dest_id}/photo`, data);/users/:user_id/destinations/:dest_id/photo
}

// Gets the image file names from the serer associated with  a traveller using fetch.
export const getImages = async (id) => {
    return Repository.get(`/travellers/${id}/photo`);
}

export const updatePersonalPhoto = async (payload) => {
    return Repository.post(`/travellers/photo/${payload.id}`, payload);
}   

// Sets the user profile picture to a new uploaded photo
export const uploadProfilePic = async (id, payload) => {
    return Repository.put(`/travellers/${id}/uploadphoto`, payload);
}

// Sets the user profile picture to a selected photo
export const setProfilePic = async (id, payload) => {
    return Repository.put(`/travellers/${id}/setphoto`, payload);
}
