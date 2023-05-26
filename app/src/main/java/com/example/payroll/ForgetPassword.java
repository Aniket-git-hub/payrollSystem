package com.example.payroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText emailEditText;
    Button sendEmailBtn;
    TextView backToLoing;

    FirebaseAuth auth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText = findViewById(R.id.emailEditText);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        backToLoing = findViewById(R.id.backToLogin);

        auth = FirebaseAuth.getInstance();

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                if(email.isEmpty()){
                    emailEditText.setError("Required");
                }else{
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                emailEditText.setText("");
                                Toast.makeText(ForgetPassword.this, "Password Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPassword.this, Login.class));
                                finish();
                            }else{
                                emailEditText.setText("");
                                Toast.makeText(ForgetPassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backToLoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassword.this, Login.class));
                finish();
            }
        });

    }
}