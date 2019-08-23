package com.vas.architecture.components;

public class Utils {
    public static String getTextFor(Throwable error) {
        if (error == null)
            return null;
        return error.getLocalizedMessage();
    }
}
