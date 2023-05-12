package com.example.payroll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn, addEmployeeBtn;
    TextView textView;
    FirebaseUser user;

    EditText employeeNameEdit, employeeAddressEdit, employeeContactEdit, employeeSalaryEdit;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logoutBtn);
        textView = findViewById(R.id.userDetails);

        employeeNameEdit = findViewById(R.id.employeeName);
        employeeAddressEdit = findViewById(R.id.employeeAddress);
        employeeContactEdit = findViewById(R.id.employeeContact);
        employeeSalaryEdit = findViewById(R.id.employeeSalary);
        addEmployeeBtn = findViewById(R.id.addEmployeeBtn);

        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else {
            textView.setText(user.getEmail());
        }

        // Initializing the dbHandler variable
        dbHandler = new DBHandler(MainActivity.this);

        addEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String employeeName = String.valueOf(employeeNameEdit.getText());
                String employeeAddress = String.valueOf(employeeAddressEdit.getText());
                String employeeContact = String.valueOf(employeeContactEdit.getText());
                String employeeSalary = String.valueOf(employeeSalaryEdit.getText());

                if(employeeName.isEmpty() && employeeAddress.isEmpty() && employeeContact.isEmpty() && employeeSalary.isEmpty()) {
                    Toast.makeText(MainActivity.this, "PLEASE FILL THE INPUTS", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewEmployee(employeeName, employeeAddress, employeeContact, Integer.valueOf(employeeSalary));

                Toast.makeText(MainActivity.this, "New Employee Added", Toast.LENGTH_SHORT).show();
                employeeNameEdit.setText("");
                employeeAddressEdit.setText("");
                employeeContactEdit.setText("");
                employeeSalaryEdit.setText("");

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}