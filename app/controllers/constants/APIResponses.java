package controllers.constants;

public class APIResponses {

    // General
    public static final String FORBIDDEN = "Forbidden: Access Denied";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String MISSING_FILE = "Missing file";
    public static final String NOT_FOUND = "Not Found";

    public static final String DESTINATION_NOT_FOUND = "Destination not found";
    public static final String LESS_THAN_TWO_DESTINATIONS = "Less than two destinations";
    public static final String TWO_SAME_DESTINATIONS_IN_A_ROW = "Two same destinations in a row";
    public static final String TRIP_NOT_FOUND = "Trip not found";
    public static final String TRAVELLER_NOT_FOUND = "Traveller not found";
    public static final String NOT_LOGGED_IN_ACCESS_DENIED = "Not Logged In: Access Denied";
    public static final String FORBIDDEN_DESTINATION_EDIT = "Permission denied: Trying to edit a public destination and not admin";
    public static final String ALBUM_OR_MEDIA_NOT_FOUND = "The existing album or existing media was not found";
    public static final String FORBIDDEN_ALBUM_DELETION = "The album does not belong to you";
    public static final String SUCCESSFUL_ALBUM_DELETION = "Album deleted";
    public static final String SUCCESSFUL_MEDIA_DELETION = "Media deleted";
    public static final String FORBIDDEN_ALBUM_UPDATE = FORBIDDEN_ALBUM_DELETION;
    public static final String SUCCESSFUL_ALBUM_UPDATE = "Album updated";
    public static final String DUPLICATE_ALBUM_NAME = "Duplicate album name";

    // Groups
    public static final String SUCCESSFUL_GROUP_MEMBER_DELETION = "Group member removed";
    public static final String GROUP_MEMBER_NOT_FOUND = "Group member not found";
    public static final String SUCCESSFUL_GROUP_DELETION = "Group removed";
    public static final String SUCCESSFUL_GROUP_UPDATE = "Group updated";
    public static final String GROUP_NOT_FOUND = "Group not found";
    public static final String DUPLICATE_GROUP_NAME = "There is already a group with this name";
    public static final String MEMBER_EXISTS_IN_GROUP = "Member already exists in the group";
    public static final String FAILED_TO_PROMOTE = "Failed to promote member to group owner";






}
