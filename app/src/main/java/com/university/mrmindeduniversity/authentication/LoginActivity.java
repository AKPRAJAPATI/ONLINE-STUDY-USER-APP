package com.university.mrmindeduniversity.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.university.mrmindeduniversity.MainActivity;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.color_main));
            getWindow().setNavigationBarColor(getColor(R.color.color_main));

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Login Account");

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),forgotpasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.registersumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userEmailEdit.getText().toString().equals("")) {
                    binding.userEmailEdit.requestFocus();
                    binding.userEmailEdit.setError("Enter your email like this abc@gmail.com");
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (!binding.userEmailEdit.getText().toString().contains("@gmail.com")) {
                    binding.userEmailEdit.requestFocus();
                    binding.userEmailEdit.setError("Email is wrong");
                    Toast.makeText(LoginActivity.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                } else if (binding.userPasswordEdit.getText().toString().equals("")) {
                    binding.userPasswordEdit.requestFocus();
                    binding.userPasswordEdit.setError("Enter your password");
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (binding.userPasswordEdit.getText().toString().length() < 8) {
                    binding.userPasswordEdit.requestFocus();
                    binding.userPasswordEdit.setError("Password more than 8 character");
                    Toast.makeText(LoginActivity.this, "Password more than 8 character", Toast.LENGTH_SHORT).show();
                }  else if (!binding.registrationKey.getText().toString().equals("umDDU06UYdPshCmLWNhzaSUNlGH2")) { // umDDU06UYdPshCmLWNhzaSUNlGH2 this is a auth key of our admin
                      binding.registrationKey.requestFocus();
                      binding.registrationKey.setError("Registration key is wrong");
                      Toast.makeText(LoginActivity.this, "Please check your registration key", Toast.LENGTH_SHORT).show();
                } else {
                    LoginAccount(binding.userEmailEdit.getText().toString(),binding.userPasswordEdit.getText().toString());
                }
            }
        });
        binding.loginnnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }

    private void LoginAccount(String email, String password) {
    progressDialog.show();
    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                progressDialog.dismiss();

                SharedPreferences sharedPreferences = getSharedPreferences("mrMinded",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key",binding.registrationKey.getText().toString());
                editor.commit();

                Toast.makeText(LoginActivity.this, "User Login Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else{
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Account not find", Toast.LENGTH_SHORT).show();
                String url = "https://mrmindeduniversity.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    });
    }
}