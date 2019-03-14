import AuthRepository from "./AuthRepository"
import ProfileRepository from "./ProfileRepository"
import DestinationEditRepository from "./DestinationEditRepository"



// Add all repositories here to access from anywhere
const repositories = {
    auth: AuthRepository,
    profile: ProfileRepository,
    destination: DestinationEditRepository
};


export const RepositoryFactory = {
    get: name => repositories[name]
};