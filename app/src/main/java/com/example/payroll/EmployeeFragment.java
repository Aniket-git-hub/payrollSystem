package com.example.payroll;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

public class EmployeeFragment extends Fragment {
    public EmployeeFragment(){
        // require a empty public constructor
    }
    ArrayList<EmployeeModal> employeeModalArrayList = new ArrayList<>();
    DBHandler dbHandler;
    EmployeeRVAdapter employeeRVAdapter;
    RecyclerView employeeRV;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        auth = FirebaseAuth.getInstance();

        dbHandler = new DBHandler(getActivity(), auth.getCurrentUser().getEmail());

         employeeModalArrayList = dbHandler.readEmployees();

        employeeRVAdapter = new EmployeeRVAdapter(employeeModalArrayList, getActivity());
         employeeRV = view.findViewById(R.id.idRVEmployees);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        employeeRV.setLayoutManager(linearLayoutManager);

        employeeRV.setAdapter(employeeRVAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        employeeModalArrayList = dbHandler.readEmployees();
        employeeRVAdapter.updateData(employeeModalArrayList);
    }



}
