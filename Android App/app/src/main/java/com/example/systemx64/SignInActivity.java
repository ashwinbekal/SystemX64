package com.example.systemx64;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

// SignInActivity.java

public class SignInActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth firebaseAuth;
    public String ophone;

    public String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        firebaseAuth = FirebaseAuth.getInstance();
        ophone=getIntent().getStringExtra("phone");
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Username=getIntent().getStringExtra("username");
        Button buttonSignIn = findViewById(R.id.buttonSignIn);

        TextView textViewSignup = findViewById(R.id.textViewsignup);
        String text = textViewSignup.getText().toString();

        // Create a SpannableString to apply different colors
        SpannableString spannableString = new SpannableString(text);

        // Set color for "New user"
        int startIndexNewUser = text.indexOf("New user");
        int endIndexNewUser = startIndexNewUser + "New user".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), startIndexNewUser, endIndexNewUser, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set color for "Signup"
        int startIndexSignup = text.indexOf("Signup");
        int endIndexSignup = startIndexSignup + "Signup".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndexSignup, endIndexSignup, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewSignup.setText(spannableString);

        buttonSignIn.setOnClickListener(v -> signInUser());

    }

    private void signInUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, go to the MapActivity
                        Intent intent = new Intent(SignInActivity.this, Policeinput.class);
                        intent.putExtra("uphone",ophone);
                        intent.putExtra("username",Username);

                        startActivity(intent);
                        finish();
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void OnSignClick(View v){
        Intent intent =new Intent(SignInActivity.this,RegisterActivity.class);
        startActivity(intent);

    }
}