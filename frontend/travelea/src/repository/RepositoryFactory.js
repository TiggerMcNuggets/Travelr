import AuthRepository from "./AuthRepository"
import ProfileRepository from "./ProfileRepository"



// Add all repositories here to access from anywhere
const repositories = {
    auth: AuthRepository,
    profile: ProfileRepository,
};


export const RepositoryFactory = {
    get: name => repositories[name]
};