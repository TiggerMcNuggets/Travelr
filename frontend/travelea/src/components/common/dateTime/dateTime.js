import moment from "moment";

let dateTime = {};

/**
 * Converts a string date into unix timestamp
 * @param date The date as a string with the format "YYYY-MM-DD"
 */
dateTime.convertStringToTimestamp = date => {
  return moment(date)
    .utc()
    .unix();
};

/**
 * Converts a unix timestamp into a string date
 * @param timestamp The timestamp (seconds since beginning of 1970)
 */
dateTime.convertTimestampToString = timestamp => {
  return (timestamp !== null) ? moment.unix(timestamp).format("YYYY-MM-DD") : 'N/A';
};

export default dateTime;
