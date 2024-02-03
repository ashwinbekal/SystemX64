package com.example.systemx64;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.systemx64.databinding.ActivityOtpVerifyBinding;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {

    private ActivityOtpVerifyBinding binding;
    private String verificationId;
    private OTP_Receiver otp_receiver;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public String phonee;
    public String email;
    public String username;
    public String password;
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        phonee=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");

        username=getIntent().getStringExtra("name");
        Log.i("iamverify",username);
        mAuth = FirebaseAuth.getInstance();

        editTextInput();

        binding.tvMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("phone")
        ));

        verificationId = getIntent().getStringExtra("verificationId");

        binding.tvResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(OtpVerifyActivity.this, "OTP Send Successfully.", Toast.LENGTH_SHORT).show();
                againOtpSend();
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.progressBarVerify.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.INVISIBLE);
                if (binding.etC1.getText().toString().trim().isEmpty() ||
                        binding.etC2.getText().toString().trim().isEmpty() ||
                        binding.etC3.getText().toString().trim().isEmpty() ||
                        binding.etC4.getText().toString().trim().isEmpty() ||
                        binding.etC5.getText().toString().trim().isEmpty() ||
                        binding.etC6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpVerifyActivity.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificationId != null) {
                        String code = binding.etC1.getText().toString().trim() +
                                binding.etC2.getText().toString().trim() +
                                binding.etC3.getText().toString().trim() +
                                binding.etC4.getText().toString().trim() +
                                binding.etC5.getText().toString().trim() +
                                binding.etC6.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            binding.progressBarVerify.setVisibility(View.VISIBLE);
                                            binding.btnVerify.setVisibility(View.INVISIBLE);
                                            rrr();



                                            Toast.makeText(OtpVerifyActivity.this, "Welcome...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(OtpVerifyActivity.this, SignInActivity.class);
                                            intent.putExtra("phone",phonee);
                                            intent.putExtra("username",username);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            binding.progressBarVerify.setVisibility(View.GONE);
                                            binding.btnVerify.setVisibility(View.VISIBLE);
                                            Toast.makeText(OtpVerifyActivity.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        autoOtpReceiver();
    }
    public void rrr(){
        Log.i("iamrrr",username);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success, update user profile with username
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)

                                    // You can also set other profile information like photo URL if needed
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileUpdateTask -> {
                                        if (profileUpdateTask.isSuccessful()) {
                                            Toast.makeText(this,"noerr",Toast.LENGTH_SHORT).show();
                                            // Profile update success, go to the MapActivity


                                        } else {
                                            Toast.makeText(this,"errorname",Toast.LENGTH_SHORT).show();
                                            // If profile update fails, display a message to the user.

                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this,"elsenouser",Toast.LENGTH_SHORT).show();

                        // If registration fails, display a message to the user.

                    }
                });



    }


    private void autoOtpReceiver() {
        otp_receiver = new OTP_Receiver();
        this.registerReceiver(otp_receiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
        otp_receiver.initListener(new OTP_Receiver.OtpReceiverListener() {
            @Override
            public void onOtpSuccess(String otp) {
                int o1 = Character.getNumericValue(otp.charAt(0));
                int o2 = Character.getNumericValue(otp.charAt(1));
                int o3 = Character.getNumericValue(otp.charAt(2));
                int o4 = Character.getNumericValue(otp.charAt(3));
                int o5 = Character.getNumericValue(otp.charAt(4));
                int o6 = Character.getNumericValue(otp.charAt(5));

                binding.etC1.setText(String.valueOf(o1));
                binding.etC2.setText(String.valueOf(o2));
                binding.etC3.setText(String.valueOf(o3));
                binding.etC4.setText(String.valueOf(o4));
                binding.etC5.setText(String.valueOf(o5));
                binding.etC6.setText(String.valueOf(o6));
            }

            @Override
            public void onOtpTimeout() {
                Toast.makeText(OtpVerifyActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void againOtpSend() {
        binding.progressBarVerify.setVisibility(View.VISIBLE);
        binding.btnVerify.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                binding.progressBarVerify.setVisibility(View.GONE);
                binding.btnVerify.setVisibility(View.VISIBLE);
                Toast.makeText(OtpVerifyActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                binding.progressBarVerify.setVisibility(View.GONE);
                binding.btnVerify.setVisibility(View.VISIBLE);
                Toast.makeText(OtpVerifyActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + getIntent().getStringExtra("phone").trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void editTextInput() {
        binding.etC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etC4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etC5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (otp_receiver != null) {
            unregisterReceiver(otp_receiver);
        }
    }


}
