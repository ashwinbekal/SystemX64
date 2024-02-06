package com.example.systemx64;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Profilenamephoto extends AppCompatActivity {
    public Button button;
    public EditText name;
    public String rname;
    public String email;
    public String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilenamephoto);
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        button=findViewById(R.id.save);
        name=findViewById(R.id.name2);

        button.setOnClickListener(view -> Onsave());


    }
    public void Onsave(){
        rname=name.getText().toString().trim();

        Intent intent= new Intent(Profilenamephoto.this,OtpSendActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        intent.putExtra("name",rname);
        startActivity(intent);

    }
}