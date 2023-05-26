package com.example.payroll;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AddNewEmployee extends AppCompatActivity {

    Button addNewEmployeeBtn;
    EditText employeeJoiningDateEdit, employeeNameEdit, employeeAgeEdit, employeeSalaryEdit, employeeAddressEdit, employeeEmailEdit, employeeContactEdit, employeeDesignationEdit;

    DBHandler dbHandler;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);

        auth = FirebaseAuth.getInstance();

        employeeJoiningDateEdit = findViewById(R.id.employeeJoiningDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        employeeJoiningDateEdit.setText(currentDate);

        employeeNameEdit = findViewById(R.id.employeeName);
        employeeAgeEdit = findViewById(R.id.employeeAge);
        employeeContactEdit = findViewById(R.id.employeeContact);
        employeeAddressEdit = findViewById(R.id.employeeAddress);
        employeeEmailEdit = findViewById(R.id.employeeEmail);
        employeeSalaryEdit = findViewById(R.id.employeeSalary);
        employeeDesignationEdit = findViewById(R.id.employeeDesignation);
        addNewEmployeeBtn = findViewById(R.id.addNewEmployeeBtn);

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
                String age = employeeAgeEdit.getText().toString();
                if (age.isEmpty()) {
                    employeeAgeEdit.setError("Age is required");
                } else {
                    employeeAgeEdit.setError(null);
                }

                String phoneNumber = employeeContactEdit.getText().toString();
                if (phoneNumber.isEmpty()) {
                    employeeContactEdit.setError("Phone number is required");
                } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    employeeContactEdit.setError("Invalid phone number");
                } else {
                    employeeContactEdit.setError(null);
                }

                String email = employeeEmailEdit.getText().toString();
                if (email.isEmpty()) {
                    employeeEmailEdit.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    employeeEmailEdit.setError("Invalid email address");
                } else {
                    employeeEmailEdit.setError(null);
                }
            }
        };

        employeeAgeEdit.addTextChangedListener(textWatcher);
        employeeContactEdit.addTextChangedListener(textWatcher);
        employeeEmailEdit.addTextChangedListener(textWatcher);

        dbHandler = new DBHandler(AddNewEmployee.this, auth.getCurrentUser().getEmail());

        addNewEmployeeBtn.setOnClickListener(v -> {
            String employeeName = String.valueOf(employeeNameEdit.getText());
            String employeeAge = String.valueOf(employeeAgeEdit.getText());
            String employeeContact = String.valueOf(employeeContactEdit.getText());
            String employeeAddress = String.valueOf(employeeAddressEdit.getText());
            String employeeEmail = String.valueOf(employeeEmailEdit.getText());
            String employeeSalary = String.valueOf(employeeSalaryEdit.getText());
            String employeeDesignation = String.valueOf(employeeDesignationEdit.getText());
            String employeeJoiningDate = String.valueOf(employeeJoiningDateEdit.getText());

            // Validate the input values
            boolean isValid = true;

            if (employeeName.isEmpty()) {
                employeeNameEdit.setError("Name is required");
                isValid = false;
            } else if (employeeName.length() < 3 || employeeName.length() > 50) {
                employeeNameEdit.setError("Name must be between 3 and 50 characters");
                isValid = false;
            } else {
                employeeNameEdit.setError(null);
            }

            if (employeeAge.isEmpty()) {
                employeeAgeEdit.setError("Age is required");
                isValid = false;
            } else {
                employeeAgeEdit.setError(null);
            }

            if (employeeContact.isEmpty()) {
                employeeContactEdit.setError("Phone number is required");
                isValid = false;
            } else if (!Patterns.PHONE.matcher(employeeContact).matches()) {
                employeeContactEdit.setError("Invalid phone number");
                isValid = false;
            } else if (!employeeContact.startsWith("+91")) {
                employeeContactEdit.setError("Phone number must be an Indian phone number");
                isValid = false;
            } else {
                employeeContactEdit.setError(null);
            }

            if (employeeEmail.isEmpty()) {
                employeeEmailEdit.setError("Email is required");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(employeeEmail).matches()) {
                employeeEmailEdit.setError("Invalid email address");
                isValid = false;
            } else {
                employeeEmailEdit.setError(null);
            }

            if (!isValid) {
                Toast.makeText(AddNewEmployee.this, "Please correct the errors and try again. ", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.addNewEmployee(employeeName,
                    Integer.parseInt(employeeAge),
                    employeeAddress,
                    employeeContact,
                    employeeEmail,
                    employeeDesignation,
                    Integer.parseInt(employeeSalary),
                    employeeJoiningDate
            );

            Toast.makeText(AddNewEmployee.this, "New Employee Added", Toast.LENGTH_SHORT).show();

            // Clear all fields
            employeeNameEdit.setText("");
            employeeAgeEdit.setText("");
            employeeContactEdit.setText("");
            employeeAddressEdit.setText("");
            employeeSalaryEdit.setText("");
            employeeEmailEdit.setText("");
            employeeDesignationEdit.setText("");
        });



    }

}