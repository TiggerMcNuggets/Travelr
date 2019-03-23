import moment from "moment";

let dateTime = {};

/**
 * Converts a string date into unix timestamp
 * @param date The date as a string with the format "YYYY-MM-DD"
 */
dateTime.convertDate = date => {
  return moment(date)
    .utc()
    .unix();
};

export default dateTime;
