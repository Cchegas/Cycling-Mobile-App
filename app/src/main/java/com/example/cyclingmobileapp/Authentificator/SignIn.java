//package com.example.cyclingmobileapp.Authentificator;
//
//import static android.content.ContentValues.TAG;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.firebase;
//import com.google.firebase.auth;
//
//import com.example.cyclingmobileapp.R;
//
//public class SignIn {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        private FirebaseAuth mAuth;
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        mAuth = FirebaseAuth.getInstance();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            reload();
//        }
//        // Initially used, they need to be implemented
//        //
//
//        String password = getPasswordFromUserInput();
//        String username = getUsernameFromUserInput();
//        Enum Role = getRoleFromUserInput();
//
//
//        private void createAccount(String username, String password) {
//            // [START create_user_with_email]
//            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth if not already done elsewhere
//
//            mAuth.createUserWithEmailAndPassword(username, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUser: success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail: failure", task.getException());
//                                Toast.makeText(YourActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                            }
//                        }
//                    });
//            // [END create_user_with_email]
//        }
//
//    }
//}