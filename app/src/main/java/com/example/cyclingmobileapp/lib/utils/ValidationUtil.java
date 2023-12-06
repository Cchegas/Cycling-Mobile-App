package com.example.cyclingmobileapp.lib.utils;

import android.content.Context;
import android.widget.Toast;

public class ValidationUtil {
    // used for test cases
    public ValidationUtil(Context context){

    }
    // fieldValue is obtained by casting context into an activity to access findViewById method to then be used
    // as an EditText datatype to access its string form.

    // Validates int numbers
    public static boolean validateInt(Context context, String fieldValue, String fieldName) {
        fieldValue = fieldValue.trim();
        if (fieldValue.isEmpty()) {
            makeToast(context, "The entry for " + fieldName + " should not be empty!");
            return false;
        }
        try {
            Integer.parseInt(fieldValue);
        } catch (NumberFormatException e) {
            makeToast(context, "The entry for " + fieldName + " should be an Integer!");
            return false;
        }
        return true;
    }

    //Validates float numbers
    public static boolean validateFloat(Context context, String fieldValue, String fieldName) {
        fieldValue = fieldValue.trim();
        if (fieldValue.isEmpty()) {
            makeToast(context, "The entry for " + fieldName + " should not be empty!");
            return false;
        }
        try {
            Float.parseFloat(fieldValue);
        } catch (NumberFormatException e) {
            makeToast(context, "The entry for " + fieldName + " should be a Float!");
            return false;
        }
        return true;
    }

    // Validates empty fields
    public static boolean validateEmpty(Context context, String fieldValue, String fieldName) {
        fieldValue = fieldValue.trim();
        if (fieldValue.isEmpty()) {
            makeToast(context, "The entry for " + fieldName + " should not be empty!");
            return false;
        }
        return true;
    }

    //validates a specific regex used for a field to match specific entries
    public static boolean validateRegex(Context context, String fieldValue, String fieldName, String regex, String regexRequirement) {
        fieldValue = fieldValue.trim();
        if (fieldValue.isEmpty()) {
            makeToast(context, "The entry for " + fieldName + " should not be empty!");
            return false;
        }
        if (!fieldValue.matches(regex)) {
            makeToast(context, "The entry for the " + fieldName + " field needs to have " + regexRequirement + ".");
            return false;
        }
        return true;
    }

    // Method used to show toast error message, can only be accessed through this class
    private static void makeToast(Context context, String toastText) {
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

}