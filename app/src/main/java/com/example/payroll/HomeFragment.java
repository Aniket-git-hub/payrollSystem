package com.example.payroll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment implements AddAttendanceRVAdapter.buttonClickListener {
    public HomeFragment(){
        // require a empty public constructor
    }

    FirebaseAuth auth;
    Button addNewEmployeeActivityLinkBtn;
    TextView dateTextView, welcomeTextView, totalPaymentInCurrentMonthTextView, highestPaymentInCurrentMonthTextView, lowestPaymentInCurrentMonthTextView;
    RecyclerView addAttendanceRV;

    AddAttendanceRVAdapter adapter;

    public DBHandler dbHandler;

    List<EmployeeModal> employeeModalList;
    private static final int REQUEST_CODE = 1;



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addAttendanceRV = view.findViewById(R.id.addAttendanceRV);
        addNewEmployeeActivityLinkBtn = view.findViewById(R.id.addNewEmployeeActivityLinkBtn);

        welcomeTextView = view.findViewById(R.id.welcomeText);
        dateTextView = view.findViewById(R.id.dateTextView);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        welcomeTextView.setText("Hi, " + user.getDisplayName());

        Locale locale = new Locale("en", "IN");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());
        dateTextView.setText(date);

        totalPaymentInCurrentMonthTextView = view.findViewById(R.id.totalPaymentInCurrentMonth);
        highestPaymentInCurrentMonthTextView = view.findViewById(R.id.highestPaymentInCurrentMonth);
        lowestPaymentInCurrentMonthTextView = view.findViewById(R.id.lowestPaymentInCurrentMonth);

        addNewEmployeeActivityLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewEmployee.class);
                startActivity(intent);
            }
        });

        dbHandler = new DBHandler(getActivity(), auth.getCurrentUser().getEmail());

        employeeModalList = dbHandler.readEmployees();
        totalPaymentInCurrentMonthTextView.setText("\u20B9" + dbHandler.getTotalPaymentInCurrentMonth());
        highestPaymentInCurrentMonthTextView.setText("\u20B9" + dbHandler.getHighestPaymentInCurrentMonth());
        lowestPaymentInCurrentMonthTextView.setText("\u20B9" + dbHandler.getLowestPaymentInCurrentMonth());

        addAttendanceRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new AddAttendanceRVAdapter(requireActivity().getApplicationContext(), employeeModalList, this);
        addAttendanceRV.setAdapter(adapter);



        return view;
    }

    @Override
    public void onButtonClick(int position, String type, int employeeId) {
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {

                    @SuppressLint("SimpleDateFormat")
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String date = dateFormat.format(new Date());
                    @SuppressLint("DefaultLocale")
                    String time = String.format("%02d:%02d:00", hourOfDay, minute1);

                    if (Objects.equals("In", type)) {
                        if (dbHandler.addIntime(employeeId, date, time)) {
                            Toast.makeText(requireActivity().getApplicationContext(), "In Successfully Added", Toast.LENGTH_SHORT).show();
                            adapter.updateButtonState(employeeId, type, time);
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "In Time Already Exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (dbHandler.addOuttime(employeeId, date, time)) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Out Successfully Added", Toast.LENGTH_SHORT).show();
                            adapter.updateButtonState(employeeId, type, time);
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Out Time Already Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                }, hour, minute, false);

        timePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        employeeModalList = dbHandler.readEmployees();
        adapter.updateData(employeeModalList);
    }
}
