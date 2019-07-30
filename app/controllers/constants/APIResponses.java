package controllers.constants;

public class APIResponses {

    public static final String DESTINATION_NOT_FOUND = "Destination not found";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String MISSING_FILE = "Missing file";
    public static final String LESS_THAN_TWO_DESTINATIONS = "Less than two destinations";
    public static final String TWO_SAME_DESTINATIONS_IN_A_ROW = "Two same destinations in a row";
    public static final String TRIP_NOT_FOUND = "Trip not found";
    public static final String TRAVELLER_NOT_FOUND = "Traveller not found";
    public static final String NOT_LOGGED_IN_ACCESS_DENIED = "Not Logged In: Access Denied";
    public static final String FORBIDDEN_DESTINATION_EDIT = "Permission denied: Trying to edit a public destination and not admin";
    public static final String ALBUM_OR_MEDIA_NOT_FOUND = "The existing album or existing media was not found";
    public static final String ALBUM_OR_MEDIA_OR_DESTINATION_NOT_FOUND = "The existing album or existing media or existing destination was not found";

    public static final String FORBIDDEN_ALBUM_DELETION = "The album does not belong to you";
    public static final String SUCCESSFUL_ALBUM_DELETION = "Album deleted";
    public static final String SUCCESSFUL_MEDIA_DELETION = "Media deleted";
    public static final String FORBIDDEN_ALBUM_UPDATE = FORBIDDEN_ALBUM_DELETION;
    public static final String SUCCESSFUL_ALBUM_UPDATE = "Album updated";
    public static final String DUPLICATE_ALBUM_NAME = "Duplicate album name";


}
