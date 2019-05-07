package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Simple helper to create dates from a given format
 */
public class Moment {

    private String pattern;

    public Moment(String pattern) {
        this.pattern = pattern;
    }

    /**
     * expects pattern to be in YYYY-MM-DD format
     *
     * @return the date object
     */
    public Date toDOBFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(this.pattern);
            return date;
        } catch (ParseException e) {
            System.err.println("Moment, toDOBFormat: Not parsable date object");
            return null;
        }
    }


    /**
     *
     * @param years
     * @return a String object in 'yyyy-MM-dd' format representing a date of birth calculated using years
     */
    public static String findDOBFromAge(Integer years) {
        LocalDate now = LocalDate.now();
        LocalDate dob = now.minusYears(years);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dob.format(dateTimeFormatter);
    }

}
