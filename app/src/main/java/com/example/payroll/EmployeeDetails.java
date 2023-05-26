package com.example.payroll;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeDetails extends AppCompatActivity {

    EditText employeeNameEdit, employeeAgeEdit, employeeSalaryEdit, employeeAddressEdit, employeeEmailEdit, employeeContactEdit, employeeDesignationEdit;
    Button employeeEditToggleBtn, employeeDeleteBtn, employeeUpdateBtn, addAttendanceBtn, changeMonthBtn;
    TextView overTimeTextView, leavesTextView, paymentTextView;
    Boolean editingEnabled = false;

    TableLayout employeeAttendanceTable;

    FirebaseAuth auth;

    DBHandler dbHandler;
    Calendar calendar;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        calendar = Calendar.getInstance();

        auth = FirebaseAuth.getInstance();

        dbHandler = new DBHandler(EmployeeDetails.this, auth.getCurrentUser().getEmail());

        Intent intent = getIntent();
        String employeeId = intent.getStringExtra("id");
        String employeeName = intent.getStringExtra("name");
        String employeeAge = intent.getStringExtra("age");
        String employeeAddress = intent.getStringExtra("address");
        String employeeContact = intent.getStringExtra("contact");
        String employeeEmail = intent.getStringExtra("email");
        String employeeDesignation = intent.getStringExtra("designation");
        String employeeSalary = intent.getStringExtra("salary");

        employeeNameEdit = findViewById(R.id.employeeName);
        employeeAgeEdit = findViewById(R.id.employeeAge);
        employeeContactEdit = findViewById(R.id.employeeContact);
        employeeAddressEdit = findViewById(R.id.employeeAddress);
        employeeEmailEdit = findViewById(R.id.employeeEmail);
        employeeSalaryEdit = findViewById(R.id.employeeSalary);
        employeeDesignationEdit = findViewById(R.id.employeeDesignation);

        addAttendanceBtn = findViewById(R.id.addAttendanceBtn);
        changeMonthBtn = findViewById(R.id.changeMonthBtn);

        employeeEditToggleBtn = findViewById(R.id.employeeEditToggleBtn);
        employeeUpdateBtn = findViewById(R.id.employeeUpdateBtn);
        employeeDeleteBtn = findViewById(R.id.employeeDeleteBtn);

        overTimeTextView = findViewById(R.id.overTimeTextView);
        leavesTextView = findViewById(R.id.leavesTextView);
        paymentTextView = findViewById(R.id.paymentTextView);

        employeeAttendanceTable = findViewById(R.id.employeeAttendanceTable);

        employeeNameEdit.setText(employeeName);
        employeeAgeEdit.setText(employeeAge);
        employeeAddressEdit.setText(employeeAddress);
        employeeContactEdit.setText(employeeContact);
        employeeEmailEdit.setText(employeeEmail);
        employeeDesignationEdit.setText(employeeDesignation);
        employeeSalaryEdit.setText(employeeSalary);

        disableEditText(employeeNameEdit);
        disableEditText(employeeAgeEdit);
        disableEditText(employeeAddressEdit);
        disableEditText(employeeContactEdit);
        disableEditText(employeeEmailEdit);
        disableEditText(employeeDesignationEdit);
        disableEditText(employeeSalaryEdit);
        disableButton(employeeUpdateBtn);
        disableButton(employeeUpdateBtn);

        employeeEditToggleBtn.setOnClickListener(v -> {
            if (!editingEnabled) {
                enableEditText(employeeNameEdit);
                enableEditText(employeeAgeEdit);
                enableEditText(employeeAddressEdit);
                enableEditText(employeeContactEdit);
                enableEditText(employeeEmailEdit);
                enableEditText(employeeDesignationEdit);
                enableEditText(employeeSalaryEdit);
                editingEnabled = true;
                enableButton(employeeUpdateBtn);
            } else {
                disableEditText(employeeNameEdit);
                disableEditText(employeeAgeEdit);
                disableEditText(employeeAddressEdit);
                disableEditText(employeeContactEdit);
                disableEditText(employeeEmailEdit);
                disableEditText(employeeDesignationEdit);
                disableEditText(employeeSalaryEdit);
                editingEnabled = false;
                disableButton(employeeUpdateBtn);
            }
        });

        employeeUpdateBtn.setOnClickListener(v -> {

            dbHandler.updateEmployee(Integer.parseInt(employeeId), String.valueOf(employeeNameEdit.getText()), Integer.parseInt(String.valueOf(employeeAgeEdit.getText())), String.valueOf(employeeAddressEdit.getText()), String.valueOf(employeeContactEdit.getText()), String.valueOf(employeeEmailEdit.getText()), String.valueOf(employeeDesignationEdit.getText()), Integer.parseInt(String.valueOf(employeeSalaryEdit.getText())));

            Toast.makeText(EmployeeDetails.this, "Employee Details Successfully Updated ", Toast.LENGTH_SHORT).show();

            employeeEditToggleBtn.performClick();

        });

        employeeDeleteBtn.setOnClickListener(v -> new AlertDialog.Builder(EmployeeDetails.this).setTitle("Delete Employee").setMessage("Are you sure you want to delete this employee?").setPositiveButton(android.R.string.yes, (dialog, which) -> {
            dbHandler.deleteEmployee(Integer.parseInt(employeeId));
            Toast.makeText(EmployeeDetails.this, "Employee Deleted", Toast.LENGTH_SHORT).show();
            finish();
        }).setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_alert).show());


        addAttendanceBtn.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a custom layout for the dialog
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Set the layout's padding
            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()
            );
            layout.setPadding(padding, padding, padding, padding);

            // Add a TextView to display the selected date
            final TextView dateTextView = new TextView(this);
            dateTextView.setText("Date: ");
            dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            layout.addView(dateTextView);

            // Set the button's layout parameters with bottom margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );

            // Add a Button to select the date
            Button selectDateButton = new Button(this);
            selectDateButton.setText("Select Date");
            // Set the button's background color
            selectDateButton.setBackgroundColor(Color.parseColor("#673AB7"));
            selectDateButton.setTextColor(Color.parseColor("#FFFFFF"));
            params.setMargins(0, 0, 0, padding);
            selectDateButton.setLayoutParams(params);

            selectDateButton.setOnClickListener(v12 -> {
                // Show a DatePickerDialog to select the date
                new DatePickerDialog(EmployeeDetails.this, (view, year1, month1, dayOfMonth) -> {
                    // Update the dateTextView with the selected date
                    String date = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    dateTextView.setText("Date: " + date);
                }, year, month, day
                ).show();
            });
            layout.addView(selectDateButton);

            // Add a TextView to display the selected intime
            final TextView intimeTextView = new TextView(this);
            intimeTextView.setText("Intime: ");
            intimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            layout.addView(intimeTextView);

            // Add a Button to select the intime
            Button selectIntimeButton = new Button(this);
            selectIntimeButton.setText("Select Intime");
            selectIntimeButton.setLayoutParams(params);
            selectIntimeButton.setOnClickListener(v1 -> {
                // Show a TimePickerDialog to select the intime
                new TimePickerDialog(EmployeeDetails.this, (view, hourOfDay, minute12) -> {
                    // Update the intimeTextView with the selected time
                    String intime = String.format("%02d:%02d:00", hourOfDay, minute12);
                    intimeTextView.setText("Intime: " + intime);
                }, hour, minute, false // Replace with the current time
                ).show();
            });
            // Set the button's background color
            selectIntimeButton.setBackgroundColor(Color.parseColor("#673AB7"));
            selectIntimeButton.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(selectIntimeButton);

            // Add a TextView to display the selected outtime
            final TextView outtimeTextView = new TextView(this);
            outtimeTextView.setText("Outtime: ");
            outtimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            layout.addView(outtimeTextView);

            // Add a Button to select the outtime
            Button selectOuttimeButton = new Button(this);
            selectOuttimeButton.setText("Select Outtime");
            selectOuttimeButton.setLayoutParams(params);
            selectOuttimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show a TimePickerDialog to select the outtime
                    new TimePickerDialog(EmployeeDetails.this, (view, hourOfDay, minute1) -> {
                        // Update the outtimeTextView with the selected time
                        String outtime = String.format("%02d:%02d:00", hourOfDay, minute1);
                        outtimeTextView.setText("Outtime: " + outtime);
                    }, hour, minute, false
                    ).show();
                }
            });
            // Set the button's background color
            selectOuttimeButton.setBackgroundColor(Color.parseColor("#673AB7"));
            selectOuttimeButton.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(selectOuttimeButton);

            // Create the dialog
            new AlertDialog.Builder(this).setTitle("Add Attendance").setView(layout).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                // User clicked the OK button
                // Get the selected date, intime and outtime from the TextViews
                String date = dateTextView.getText().toString().substring(6);
                String intime = intimeTextView.getText().toString().substring(8);
                String outtime = outtimeTextView.getText().toString().substring(9);

                // Calling the addAttendance method to add or update the attendance record
                dbHandler.addAttendance(Integer.parseInt(employeeId), date, intime, outtime);
                Toast.makeText(EmployeeDetails.this, "Successfully Added", Toast.LENGTH_SHORT).show();
            }).setNegativeButton(android.R.string.cancel, null).show();


        });

        changeMonthBtn.setOnClickListener(v -> {

            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            // Create a custom layout for the dialog
            LinearLayout layout = new LinearLayout(EmployeeDetails.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            // Center the NumberPickers in the layout
            layout.setGravity(Gravity.CENTER);

            // Add a NumberPicker for the month
            final NumberPicker monthPicker = new NumberPicker(EmployeeDetails.this);
            monthPicker.setMinValue(0);
            monthPicker.setMaxValue(11);
            monthPicker.setValue(month);
            monthPicker.setDisplayedValues(new String[] {
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            });
            layout.addView(monthPicker);

            // Add a NumberPicker for the year
            final NumberPicker yearPicker = new NumberPicker(EmployeeDetails.this);
            yearPicker.setMinValue(1900);
            yearPicker.setMaxValue(2100);
            yearPicker.setValue(year);
            layout.addView(yearPicker);

            // Create the dialog
            new AlertDialog.Builder(EmployeeDetails.this)
                    .setTitle("Select Month")
                    .setView(layout)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked the OK button
                            // Get the selected month and year
                            int selectedMonth = monthPicker.getValue();
                            int selectedYear = yearPicker.getValue();

                            // Format the selected month and year as "MMMM yyyy"
                            calendar.set(selectedYear, selectedMonth, 1);
                            String monthYear = new SimpleDateFormat("MMMM yyyy").format(calendar.getTime());
                            String monthYearForDB = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());

                            // Set the text of the selectMonthButton to the selected month and year
                            changeMonthBtn.setText(monthYear);

                            // add code for getting current month data here
                            reloadDateMonthChanged(Integer.parseInt(employeeId),monthYearForDB );

                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        });

        String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());


        leavesTextView.setText(String.valueOf(dbHandler.getMissingAttendanceCountInCurrentMonth(Integer.parseInt(employeeId), currentMonth, calendar.THURSDAY)));
        overTimeTextView.setText(String.valueOf(dbHandler.getOvertimeInCurrentMonth(Integer.parseInt(employeeId), currentMonth)));
        paymentTextView.setText("\u20B9" + dbHandler.getPaymentInCurrentMonth(Integer.parseInt(employeeId), currentMonth));

        updateAttendanceTable(employeeAttendanceTable, dbHandler.getAttendanceForCurrentMonth(Integer.parseInt(employeeId), currentMonth));

    }

    @SuppressLint("SetTextI18n")
    private void reloadDateMonthChanged(int employeeId, String monthYear){
        leavesTextView.setText(String.valueOf(dbHandler.getMissingAttendanceCountInCurrentMonth(employeeId, monthYear, calendar.THURSDAY)));
        overTimeTextView.setText(String.valueOf(dbHandler.getOvertimeInCurrentMonth(employeeId, monthYear)));
        paymentTextView.setText("\u20B9" + dbHandler.getPaymentInCurrentMonth(employeeId, monthYear));
        updateAttendanceTable(employeeAttendanceTable, dbHandler.getAttendanceForCurrentMonth(employeeId, monthYear));
    }

    private void updateAttendanceTable(TableLayout tableLayout, List<AttendanceModal> attendanceList){
        // Remove all rows except the first row (header row) from the table layout
        if (tableLayout.getChildCount() > 1) {
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        }
        for (int i = 0; i < attendanceList.size(); i++) {
            AttendanceModal attendance = attendanceList.get(i);
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(9f);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (6 * scale + 0.5f);
            row.setPadding(0, dpAsPixels, 0, dpAsPixels);

            TextView serialNoTextView = new TextView(this);
            serialNoTextView.setText(String.valueOf(i + 1));
            serialNoTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            serialNoTextView.setGravity(Gravity.CENTER);
            serialNoTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            row.addView(serialNoTextView);

            TextView dateTextView = new TextView(this);
            try {
                dateTextView.setText(attendance.getDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            dateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            dateTextView.setGravity(Gravity.CENTER);
            dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            row.addView(dateTextView);

            TextView intimeTextView = new TextView(this);
            try {
                intimeTextView.setText(attendance.getIntime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            intimeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            intimeTextView.setGravity(Gravity.CENTER);
            intimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            row.addView(intimeTextView);

            TextView outtimeTextView = new TextView(this);
            try {
                outtimeTextView.setText(attendance.getOuttime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            outtimeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            outtimeTextView.setGravity(Gravity.CENTER);
            outtimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            row.addView(outtimeTextView);

            TextView hoursTextView = new TextView(this);
            hoursTextView.setText(String.valueOf(attendance.getHours()));
            hoursTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            hoursTextView.setGravity(Gravity.CENTER);
            hoursTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            row.addView(hoursTextView);

            tableLayout.addView(row);
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setFocusableInTouchMode(false);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setFocusableInTouchMode(true);
    }

    private void disableButton(Button btn) {
        btn.setEnabled(false);
        btn.setVisibility(View.GONE);
    }

    private void enableButton(Button btn) {
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
    }

}