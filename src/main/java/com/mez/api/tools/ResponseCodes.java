package com.mez.api.tools;

public abstract class ResponseCodes {
    public static final byte SUCCESS = 1;
    public static final byte DATABASE_ERROR = -1;
    public static final byte INCORRECT_USERNAME_OR_PASSWORD = -2;
    public static final byte UNAUTHORISED = -3;
    public static final byte NO_ACCESS = -4;
    public static final byte CLOUDINARY_ERROR = -7;
    public static final byte UNKNOWN_ERROR = -8;
}
