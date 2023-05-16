package com.example.payroll;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ViewEmployees extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<EmployeeModal> employeeModalArrayList;
    private DBHandler dbHandler;
    private EmployeeRVAdapter employeeRVAdapter;
    private RecyclerView employeeRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        // initializing our all variables.
        employeeModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(ViewEmployees.this);

        // getting our course array
        // list from db handler class.
        employeeModalArrayList = dbHandler.readEmployees();

        // on below line passing our array list to our adapter class.
        employeeRVAdapter = new EmployeeRVAdapter(employeeModalArrayList, ViewEmployees.this);
        employeeRV = findViewById(R.id.idRVEmployees);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewEmployees.this, RecyclerView.VERTICAL, false);
        employeeRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        employeeRV.setAdapter(employeeRVAdapter);
    }
}
