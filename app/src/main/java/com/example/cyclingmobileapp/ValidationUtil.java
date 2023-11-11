package com.example.cyclingmobileapp;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class ValidationUtil {

    public ValidationUtil(){ // class is only used for access its methods, not for creating instances

    }

    // fieldValue is obtained by casting context into an activity to access findViewById method to then be used
    // as an EditText datatype to access its string form.

    // Validates int numbers
    public boolean validateInt(Context context, String fieldValue, String fieldName){

        if(fieldValue.isEmpty()){
            makeToast(context, "the entry for " + fieldName + " should not be empty!");
            return false;
        }
        try{
            Integer.parseInt(fieldValue);
        } catch (NumberFormatException e){
            makeToast(context, "the entry for " + fieldName + " should be an Integer!");
            return false;
        }
        return true;
    }

    //Validates float numbers
    public boolean validateFloat(Context context, String fieldValue, String fieldName){

        if(fieldValue.isEmpty()){
            makeToast(context, "the entry for " + fieldName + " should not be empty!");
            return false;
        }

        try{
            Float.parseFloat(fieldValue);
        } catch (NumberFormatException e){
            makeToast(context, "the entry for " + fieldName + " should be a Float!");
            return false;
        }
        return true;
    }

    //Validates empty fields
    public boolean validateEmpty(Context context, String fieldValue, String fieldName){

        if(fieldValue.isEmpty()){
            makeToast(context, "the entry for " + fieldName + " should not be empty!");
            return false;
        }
        return true;
    }

    //validates a specific regex used for a field to match specific entries
    public boolean validateRegex(Context context, String fieldValue, String fieldName, String regex, String regexrequirement){

        if(fieldValue.isEmpty()){
            makeToast(context, "the entry for " + fieldName + " should not be empty!");
            return false;
        }

        if(!fieldValue.matches(regex)){
            makeToast(context, "the entry for the field: " + fieldName + " needs to have " + regexrequirement);
            return false;
        }
        return true;
    }

    //method used to show toast error message, can only be accessed through this class
    private void makeToast(Context context, String toastText) {
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

}