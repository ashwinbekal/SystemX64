package com.example.systemx64;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() == null) {

            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(MainActivity.this,Policeinput.class));
        }







    }
}