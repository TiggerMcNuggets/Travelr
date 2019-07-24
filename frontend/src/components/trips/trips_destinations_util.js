import { RepositoryFactory } from "../../repository/RepositoryFactory";
let tripRepository = RepositoryFactory.get("trip");
import moment from "moment";

export const getChildrenCount = (destinations, parent) => {
    let i = parent.ordinal + 1;
    while (i < destinations.length && destinations[i].depth > parent.depth) {
        i += 1;
    }
    return i - parent.ordinal - 1;
};

export const getDepthData = (depth) => {
    const moduloDepth = depth % 3;
    const data = {large: false, number: depth + 1};
    if (moduloDepth === 0) {
        data.color = "blue";
        data.large = true;
    }
    else if (moduloDepth === 1) {
        data.color = "red";
    }
    else {
        data.color = "green";
    }
    return data;
};

export const isPromotable = (destinations, index) => {
    if (index === 0) return false;
    if (index === destinations.length) return false;
    const child = destinations[index];
    const parent = destinations[index - 1];
    console.log();
    return (child.depth - parent.depth) < 1;
};

export const isDemotable = (destinations, index) => {
    if (index === 0) return false; // cannot demote first destination
    if (index === (destinations.length - 1)) return false; // cannot demote last one as it also cannot be promoted
    if (destinations[index].depth === 0) return false; // cannot demote destination that is part of main one
    const parentParent = destinations[index - 1];
    const parent = destinations[index];
    const child = destinations[index + 1];
    return ((parent.depth - child.depth) > -1 && (parentParent.depth - parent.depth) < 1);

};

/**
 * Checks if the update trip form passes validation
 * If it does then updates trip and updates the view trip page
 */
export const updateTrip =  (userId, tripId, tripBody, userDestinations) => {
    // if (this.$refs.form.validate()) {
        const trip = tripAssembler(tripBody, userDestinations);
        tripRepository
            .updateTrip(userId, parseInt(tripId), trip)
            .then(() => {
                const url = `/users/${this.id}/trips/${parseInt(this.passedTrip)}`;
                this.rollbackCheckpoint(
                    'PUT',
                    {
                        url: url,
                        body: trip
                    },
                    {
                        url: url,
                        body: this.rollbackPreviousBody
                    }
                );

                // Update previous body to be used for the next checkpoints reaction
                this.rollbackSetPreviousBody({...trip});
                this.updateViewTripPage();
            })
            .catch(e => {
                console.log(e);
            });
    // }
};

/**
 * Creates a trip object from the data passed that conforms with the API specs
 **/
const tripAssembler = (tripBody, userDestinations) => {
    let trip = { name: tripBody.name, destinations: [] };
    tripBody.destinations.forEach((destination, index) => {
        const destById = userDestinations.find(

            dest => {
                return destination.customName === dest.name
            }
        );
        console.log("destById", destById);
        trip.destinations.push({
            customName: destination.title,
            id: destById.id,
            ordinal: index,
            depth: destination.depth,
            arrivalDate: moment(destination.arrivalDate).unix(),
            departureDate: moment(destination.departureDate).unix()
        });
    });
    return trip;
};