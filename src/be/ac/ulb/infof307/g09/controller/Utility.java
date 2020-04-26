package be.ac.ulb.infof307.g09.controller;

import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Collection of utility functions used across all layers
 */
public class Utility {

    private Utility() {
    }

    /**
     * @return the timeStamp
     */
    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * Checks if any Object in a series is null
     *
     * @param obj the series of Objects
     */
    public static void checkObjects(Object... obj) throws FatalException {
        for (Object o : obj) {
            if (o == null) {
                throw new FatalException("Object is null");
            }
        }
    }

    /**
     * Check if a String is empty.
     *
     * @param str     String to check.
     * @param varName Name of the variable, to be used in case a BizzException is thrown.
     * @throws BizzException In case the String is empty.
     */
    public static void checkString(String str, String varName) throws BizzException {
        if (str == null || str.equals("")) {
            throw new BizzException(varName + " is empty");
        }
    }

}
