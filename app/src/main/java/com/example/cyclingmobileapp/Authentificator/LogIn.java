//package com.example.cyclingmobileapp.Authentificator;
//
//import static androidx.core.content.ContextCompat.startActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.cyclingmobileapp.MainActivity;
//import com.example.cyclingmobileapp.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.firebase.auth.AuthResult;
//
//public class LogIn {
//    private EditText username, password;
//
//
//
//    public boolean statusValidate() {
//        if (username.getText().toString().equals("")) {
//            throw new IllegalArgumentException("Name cannot be empty");
//
//        }
//        if (password.getText().toString().equals("")) {
//            throw new IllegalArgumentException("Password cannot be empty");
//
//        }
//
//        this.username = username;
//        this.password = password;
//
//        return true;
//
//    }
//
//    public void validate(final String userName, String userPassword, final String userType) {
//
//        private void signIn(String username, String password){
//            // [START sign_in_with_email]
//            mAuth.signInWithEmailAndPassword(username, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                            }
//                        }
//                    });
//            // [END sign_in_with_email]
//        }
//    }
//}
//
//
