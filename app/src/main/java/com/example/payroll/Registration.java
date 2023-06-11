package com.example.payroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    TextInputEditText emailEditText, passwordEditText, editTextDisplayName;
    ImageView togglePasswordVisibility;
    Button registerBtn;
    TextView textview;

    ProgressBar progressBar;

    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);
        textview = findViewById(R.id.loginNow);
        editTextDisplayName = findViewById(R.id.displayName);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        togglePasswordVisibility.setOnClickListener(v -> {
            if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye);
            } else {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_closed);
            }
            passwordEditText.setSelection(Objects.requireNonNull(passwordEditText.getText()).length());
        });

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validate the input values
                String email = emailEditText.getText().toString();
                if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid email address");
                } else {
                    emailEditText.setError(null);
                }

                String password = passwordEditText.getText().toString();
                if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                } else if (password.length() < 5 || password.length() > 10) {
                    passwordEditText.setError("Password must be between 5 and 10 characters");
                } else {
                    passwordEditText.setError(null);
                }
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);


        registerBtn.setOnClickListener(v -> {
            // Get the input values
            progressBar.setVisibility(View.VISIBLE);
            String email, password, name;
            email = String.valueOf(emailEditText.getText());
            password = String.valueOf(passwordEditText.getText());
            name = String.valueOf(editTextDisplayName.getText());

            // Validate the input values
            boolean isValid = true;
            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Invalid email address");
                isValid = false;
            }
            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                isValid = false;
            } else if (password.length() < 5 || password.length() > 10) {
                passwordEditText.setError("Password must be between 5 and 10 characters");
                isValid = false;
            }

            if (isValid) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Registration.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                                editTextDisplayName.setText("");
                                                emailEditText.setText("");
                                                passwordEditText.setText("");
                                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Registration.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

    }
}