package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inflateParticipantAccountInputViews(null);
    }

    public void onLoginButtonClick(View view) {
        Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    public void inflateParticipantAccountInputViews(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.signupVariableInputLayout);
        layout.removeAllViews();

        EditText fNameInput = createEditTextInput("First name");
        EditText lNameInput = createEditTextInput("Last name");

        layout.addView(fNameInput);
        layout.addView(lNameInput);
    }

    public void inflateClubAccountInputViews(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.signupVariableInputLayout);
        layout.removeAllViews();

        EditText clubNameInput = createEditTextInput("Club name");

        layout.addView(clubNameInput);
    }

    private EditText createEditTextInput(String editTextInputHint) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, dpToPx(16));
        editText.setLayoutParams(layoutParams);
        editText.setMinHeight(dpToPx(48));
        editText.setHint(editTextInputHint);

        return editText;
    }

    private int dpToPx(int dp) {
        return (int) (dp * this.getResources().getDisplayMetrics().density);
    }
}