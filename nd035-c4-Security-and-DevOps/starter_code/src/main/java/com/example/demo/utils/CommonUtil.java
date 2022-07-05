package com.example.demo.utils;

public final class CommonUtil {

    public static boolean isEmpty (Object object){
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isPresent (Object object){
        return !isEmpty(object);
    }
}
