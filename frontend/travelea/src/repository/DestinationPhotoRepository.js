import Repository from "./Repository";


// Basic fetch POST function to upload the image to the server.
export const storeDestinationImage = async (user_id, dest_id, data) => {
    return Repository.post(`/users/${user_id}/destinations/${dest_id}/photo`, data);
}

// Gets the image file names from the serer associated with  a traveller using fetch.
export const getImages = async (user_id, dest_id) => {
    return Repository.get(`/users/${user_id}/destinations/${dest_id}/photo`);
}

export const updateDestinationPhoto = async (payload) => {
    return Repository.put(`/destinations/photo/${payload.id}`, payload);
}

// Gets the image file names from the serer associated with  a traveller using fetch.
export const getSingleImage = async (dest_id, filename) => {
    return Repository.get(`/destinations/${dest_id}/photo/${filename}`);
}

// Sets the user profile picture to a new uploaded photo
export const uploadProfilePic = async (id, payload) => {
    return Repository.put(`/travellers/${id}/uploadphoto`, payload);
}

// Sets the user profile picture to a selected photo
export const setProfilePic = async (id, payload) => {
    return Repository.put(`/travellers/${id}/setphoto`, payload);
}
