package com.example.assignment3_akorchynskyi_200524341.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment3_akorchynskyi_200524341.R;
import com.example.assignment3_akorchynskyi_200524341.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;

    String userEmail;

    String userPassword;

    String userPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.textViewToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentObj = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentObj);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    userEmail = binding.editTextEmail.getText().toString();
                    userPassword = binding.editTextPassword.getText().toString();
                    userPasswordConfirmation = binding.editTextConfirmPassword.getText().toString();
                }
                catch (Exception e) {
                    Log.d("registration", e.getMessage());
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }



                if (userEmail.isBlank() || userPassword.isBlank() || userPasswordConfirmation.isBlank()) {

                    Toast.makeText(RegisterActivity.this, "All fields must be filled out", Toast.LENGTH_SHORT).show();

                } else {

                    if (userPassword.equals(userPasswordConfirmation)) {
                        Log.d("registration", userEmail);
                        Log.d("registration", userPassword);

                        registerUser(userEmail, userPassword);

                        Log.d("registration", "after register");


                    } else {
                        Toast.makeText(RegisterActivity.this, "Password and Password Confirmation must be the same", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    }

    private void registerUser (String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // sign in success
                            Log.d("tag", "registerWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();

                            // now navigate to LoginActivity after successful registration
                            Intent intentObj = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentObj);

                        } else {

                            Log.d("tag", "registerWithEmail:failure", task.getException());
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}