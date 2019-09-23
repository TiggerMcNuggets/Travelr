
import moment from "moment";
import dateTime from "../common/dateTime/dateTime";

/**
 * A destination is considered the parent of another if between
 * the 2 destinations there is no other destination with destination.depth >= parent.depth
 *
 * @param destinations a list of destinations objects
 * @param parent the destination of which we want to get the list of children
 * @returns {number} the number of children
 */
export const getChildrenCount = (destinations, parent) => {
    let i = parent.ordinal + 1;
    while (i < destinations.length && destinations[i].depth > parent.depth) {
        i += 1;
    }
    return i - parent.ordinal - 1;
};

/**
 * UI related, this are the characteristics displayed for each destination within the timeline object
 * @param depth {number}
 * @returns {{number: number, large: boolean, color: string}}
 */
export const getDepthData = (depth) => {
    const moduloDepth = depth % 3;
    const data = {large: false, number: depth + 1, color: ""};
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

/**
 * By promotobale, it is intended a destination that can go from depth n to depth n+1
 * @param destinations list of destinations
 * @param index of destination
 * @returns {boolean}
 */
export const isPromotable = (destinations, index) => {
    if (index === 0) return false;
    if (index === destinations.length) return false;
    const child = destinations[index];
    const parent = destinations[index - 1];
    return (child.depth - parent.depth) < 1;
};

/**
 * By demotable, it is intended a destination that can go from depth n to depth n-1
 * @param destinations list of destinations
 * @param index of destination
 * @returns {boolean}
 */
export const isDemotable = (destinations, index) => {
    if (index === 0) return false; // cannot demote first destination
    if (destinations[index].depth === 0) return false; // cannot demote destination that is part of main one
    if (index === (destinations.length - 1)) return false; // cannot demote last one as it also cannot be promoted
    const parent = destinations[index];
    const parentParent = destinations[index - 1];
    const child = destinations[index + 1];
    return ((parent.depth - child.depth) > -1 && (parentParent.depth - parent.depth) < 1);
};

/**
 * Creates a trip object from the data passed that conforms with the API specs
 **/
export const tripAssembler = (tripBody) => {
  let trip = { name: tripBody.trip.name, nodes: [] };
  tripBody.trip.nodes.forEach((node, index) => {
    trip.nodes.push({
      arrivalDate: moment(node.arrivalDate).unix(),
      departureDate: moment(node.departureDate).unix(),
      destination: {...node.destination},
      id: node.id,
      name: node.name,
      ordinal: index,
      type: node.type
    });
  });
  return trip;
};

/**
 * Converts trip into trip with dates
 * @param trip The trip
 * @return The trip
 */
export const tripWithDates = (trip) => {
  // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
  for (let i = 0; i < trip.nodes.length; i++) {
    if (trip.nodes[i].type === "destination") {
      trip.nodes[i].arrivalDateMenu = false;
      trip.nodes[i].departureDateMenu = false;
      if (trip.nodes[i].arrivalDate !== 0) {
        trip.nodes[i].arrivalDate = dateTime.convertTimestampToString(
          trip.nodes[i].arrivalDate
        );
      } else {
        trip.nodes[i].arrivalDate = null;
      }
      if (trip.nodes[i].departureDate !== 0) {
        trip.nodes[i].departureDate = dateTime.convertTimestampToString(
          trip.nodes[i].departureDate
        );
      } else {
        trip.nodes[i].departureDate = null;
      }
    }
  }

  return trip;
}

/**
 * converts trip into trip view
 * @param userId The user's id
 * @param tripId The trip's id
 * @return The viewable trip
 */
export const updateViewTripPage = (trip) => {


  // Sorts the destinations ensure they are in the order of their ordinal
  let orderedDests = trip.trip.nodes.sort(function(a, b) {
    return a.ordinal - b.ordinal;
  });

  trip.destinations = orderedDests;
  // Converts the timestamps from unix utc to locale time. If the timestamp is null allows it to remain null.
  for (let i = 0; i < trip.destinations.length; i++) {
    trip.destinations[i].expanded = false;
    if (trip.destinations[i].arrivalDate) {
      trip.destinations[i].arrivalDate = dateTime.convertTimestampToString(
        trip.destinations[i].arrivalDate
      );
    } else {
      trip.destinations[i].arrivalDate = null;
    }
    if (trip.destinations[i].departureDate) {
      trip.destinations[
        i
      ].departureDate = dateTime.convertTimestampToString(
        trip.destinations[i].departureDate
      );
    } else {
      trip.destinations[i].departureDate = null;
    }
  }
  return trip
}

/**
 * Checks if we have any identical destinations that are consecutive before updating trip
 */
export const noAdjacentIdenticalDestinations = (trip) => {
  for (let i = 0; i < trip.trip.nodes.length - 1; i++) {
    if (
      trip.trip.nodes[i].type === "destination" &&
      trip.trip.nodes[i + 1].type === "destination"
    ) {
      if (
        trip.trip.nodes[i].destination.id ===
        trip.trip.nodes[i + 1].destination.id
      ) {
        //same consecutive destination
        return false;
      }
    }
  }
  return true;
}