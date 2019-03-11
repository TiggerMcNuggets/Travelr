import AuthRepository from "./AuthRepository"


// Add all repositories here to access from anywhere
const repositories = {
    auth: AuthRepository
};


export const RepositoryFactory = {
    get: name => repositories[name]
};