package com.example.systemx64;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.systemx64.databinding.ActivityOtpSendBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpSendActivity extends AppCompatActivity {

    private ActivityOtpSendBinding binding;
    private FirebaseAuth mAuth;
    public String rmail;
    public String rpass;
    public String rnam;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rmail=getIntent().getStringExtra("email");
        rpass=getIntent().getStringExtra("password");
        rnam=getIntent().getStringExtra("name");
        Log.i("iamsend",rnam);


        mAuth = FirebaseAuth.getInstance();

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etPhone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpSendActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (binding.etPhone.getText().toString().trim().length() != 10) {
                    Toast.makeText(OtpSendActivity.this, "Type valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    otpSend();
                }
            }
        });
    }

    private void otpSend() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSend.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpSendActivity.this, OtpVerifyActivity.class);
                intent.putExtra("phone", binding.etPhone.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("email",rmail);
                intent.putExtra("password",rpass);
                intent.putExtra("name",rnam);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + binding.etPhone.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}