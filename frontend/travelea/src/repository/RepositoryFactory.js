import AuthRepository from "./AuthRepository"
import ProfileRepository from "./ProfileRepository"
import DestinationRepository from "./DestinationRepository"
import TripRepository from "./TripRepository"
// import PersonalPhotoRepository from "./PersonalPhotosRepository"
import UserRepository from "./UserRepository";
import NationalityRepository from "./NationalityRepository";
import TravellerTypesRepository from "./TravellerTypesRepository";

// Add all repositories here to access from anywhere
const repositories = {
    auth: AuthRepository,
    profile: ProfileRepository,
    destination: DestinationRepository,
    trip: TripRepository,
    // personalPhoto: PersonalPhotoRepository,
    user: UserRepository,
    nationality: NationalityRepository,
    travellerType: TravellerTypesRepository
};


export const RepositoryFactory = {
    get: name => repositories[name]
};