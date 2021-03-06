package com.spg.friendservice.validation;

public class ValidationMessage {

    public static final String FRIENDS_CANNOT_BE_NULL = "Friends' email cannot be null.";
    public static final String EMAILS_CANNOT_BE_NULL = "Email cannot be null.";
    public static final String REQUESTOR_EMAIL_CANNOT_BE_NULL = "Requestor email cannot be null.";
    public static final String TARGET_EMAIL_CANNOT_BE_NULL = "Target email cannot be null.";
    public static final String USER_NOT_FOUNT = "User not fount.";
    public static final String REQUESTOR_NOT_EXIST = "Requestor is not exist.";
    public static final String TARGET_NOT_EXIST = "Target is not exist.";
    public static final String DUPLICATE_SUBSCRIPTION = "Requestor(%s) had already subscribed to target (%s).";
    public static final String DUPLICATE_BLACKLIST = "Requestor(%s) had already blacklisted target (%s).";
    public static final String CANNOT_SUBSCRIBE_TO_ONESELF = "Cannot subscribe to oneself.";
    public static final String CANNOT_BLACKLIST_ONESELF = "Cannot blacklist oneself.";
    public static final String CANNOT_ADD_FRIENDS_WITHIN_BLACKLIST = "Cannot add friends within blacklist.";
    public static final String SENDER_CONNOT_BE_NULL = "Sender cannot be null..";

}
