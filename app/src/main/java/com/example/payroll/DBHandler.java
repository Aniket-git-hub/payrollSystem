package com.example.payroll;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "payroll";
    private static final int DB_VERSION = 1;
    private static final String TABLE_EMPLOYEE = "employee";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "employee_name";
    private static final String ADDRESS_COL = "employee_address";
    private static final String CONTACT_COL = "employee_contact_no";
    private static final String SALARY_COL = "employee_salary";
    private static final String EMAIL_COL = "employee_email";
    private static final String DESIGNATION_COL = "employee_designation";
    private static final String AGE_COL = "employee_age";
    private static final String JOINING_DATE_COL = "employee_joining_date";


    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String DATE_COL = "date";
    private static final String IN_TIME_COL = "in_time";
    private static final String OUT_TIME_COL = "out_time";


    public DBHandler(Context context, String email) {
        super(context, getDatabaseName(email), null, DB_VERSION);
    }

    private static String getDatabaseName(String email) {
        String[] emailParts = email.split("@");
        return emailParts[0] + "Database" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + AGE_COL + " INT,"
                + ADDRESS_COL + " TEXT,"
                + CONTACT_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + DESIGNATION_COL + " TEXT,"
                + SALARY_COL + " INT, "
                + JOINING_DATE_COL + " TEXT" +")";
        db.execSQL(CREATE_EMPLOYEE_TABLE);

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " +
                TABLE_ATTENDANCE + "("
                + ID_COL + " INTEGER ," + DATE_COL
                + " TEXT," + IN_TIME_COL + " TEXT,"
                + OUT_TIME_COL + " TEXT,"
                + "FOREIGN KEY(" + ID_COL + ") REFERENCES "
                + TABLE_EMPLOYEE + "(" + ID_COL + "))";
        db.execSQL(CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }

    public void addNewEmployee(String employeeName,
                               int employeeAge,
                               String employeeAddress,
                               String employeeContact,
                               String employeeEmail,
                               String employeeDesignation,
                               int employeeSalary, String employeeJoiningDate
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, employeeName);
        values.put(AGE_COL, employeeAge);
        values.put(DESIGNATION_COL, employeeDesignation);
        values.put(EMAIL_COL, employeeEmail);
        values.put(ADDRESS_COL, employeeAddress);
        values.put(CONTACT_COL, employeeContact);
        values.put(SALARY_COL, employeeSalary);
        values.put(JOINING_DATE_COL, employeeJoiningDate);

        db.insert(TABLE_EMPLOYEE, null, values);

        db.close();
    }


    public ArrayList<EmployeeModal> readEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        String query = "SELECT e.*, a." + IN_TIME_COL + ", a." + OUT_TIME_COL+" FROM " + TABLE_EMPLOYEE + " e LEFT JOIN " + TABLE_ATTENDANCE + " a ON e." + ID_COL +" = a."+ ID_COL +" AND a.date = ?";
        Cursor cursorEmployee = db.rawQuery(query, new String[]{currentDate});

        ArrayList<EmployeeModal> employeeModalArrayList = new ArrayList<>();

        if (cursorEmployee.moveToFirst()) {
            do {
                EmployeeModal employeeModal = new EmployeeModal(
                        cursorEmployee.getInt(0),
                        cursorEmployee.getString(1),
                        cursorEmployee.getInt(2),
                        cursorEmployee.getString(3),
                        cursorEmployee.getString(4),
                        cursorEmployee.getString(5),
                        cursorEmployee.getString(6),
                        cursorEmployee.getInt(7),
                        cursorEmployee.getString(8)
                );
                employeeModal.setInTime(cursorEmployee.getString(9));
                employeeModal.setOutTime(cursorEmployee.getString(10));
                employeeModalArrayList.add(employeeModal);
            } while (cursorEmployee.moveToNext());
        }
        cursorEmployee.close();
        return employeeModalArrayList;
    }

    public void updateEmployee(int id, String name, int age, String address, String phone, String email, String designation, int salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(AGE_COL, age);
        values.put(ADDRESS_COL, address);
        values.put(CONTACT_COL, phone);
        values.put(EMAIL_COL, email);
        values.put(DESIGNATION_COL, designation);
        values.put(SALARY_COL, salary);


        db.update(TABLE_EMPLOYEE, values, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean addInTime(int employeeId, String date, String inTime) {
        // Check if a record for the given date already exists
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + ID_COL + " = ?" +
                " AND " + DATE_COL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), date});
        boolean recordExists = cursor.moveToFirst();
        cursor.close();

        // If the record does not exist, insert a new record
        if (!recordExists) {
            ContentValues values = new ContentValues();
            values.put(ID_COL, employeeId);
            values.put(DATE_COL, date);
            values.put(IN_TIME_COL, inTime);
            db.insert(TABLE_ATTENDANCE, null, values);
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public boolean addOutTime(int employeeId, String date, String outTime) {
        // Check if a record for the given date already exists and if the outTime field is empty
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + ID_COL + " = ?" +
                " AND " + DATE_COL + " = ?" +
                " AND " + OUT_TIME_COL + " IS NULL" +
                " AND " + IN_TIME_COL + " IS NOT NULL";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), date});
        boolean canUpdate = cursor.moveToFirst();
        cursor.close();

        // If the record exists and the outTime field is empty, update it
        if (canUpdate) {
            ContentValues values = new ContentValues();
            values.put(OUT_TIME_COL, outTime);
            db.update(TABLE_ATTENDANCE, values, ID_COL + " = ? AND " + DATE_COL + " = ?", new String[]{String.valueOf(employeeId), date});
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }


    public List<AttendanceModal> getAttendanceForCurrentMonth(int employeeId, String currentMonthYear) {
        List<AttendanceModal> attendanceList = new ArrayList<>();
        String[] dateParts = currentMonthYear.split("-");
        String month = dateParts[1];
        String year = dateParts[0];

        // Create the query
        String query = "SELECT " + ID_COL + ", " + DATE_COL + ", " + IN_TIME_COL + ", " + OUT_TIME_COL + "," +
                "CASE WHEN " + IN_TIME_COL + " = " + OUT_TIME_COL + " THEN 24 ELSE round((strftime('%s', CASE WHEN "
                + OUT_TIME_COL + " < " + IN_TIME_COL + " THEN datetime(" + DATE_COL + " || ' ' || " + OUT_TIME_COL + ", '+1 day')" +
                " ELSE datetime(" + DATE_COL + " || ' ' || " + OUT_TIME_COL + ") END) - " +
                "strftime('%s', datetime(" + DATE_COL + " || ' ' || " + IN_TIME_COL + "))) / 3600.0, 2) END AS hours " +
                " FROM " + TABLE_ATTENDANCE +
                " WHERE " + ID_COL + " = ?" +
                " AND strftime('%m', " + DATE_COL + ") = ?" +
                " AND strftime('%Y', " + DATE_COL + ") = ? " +
                " ORDER BY " + DATE_COL +" DESC ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), String.valueOf(month), String.valueOf(year)});

        // Loop through the results and create Attendance objects
        if (cursor.moveToFirst()) {
            do {
                AttendanceModal attendance = new AttendanceModal();
                attendance.setEmployeeId(Integer.parseInt(cursor.getString(0)));
                attendance.setDate(formatDate(cursor.getString(1)));
                String inTime = cursor.getString(2);
                String outTime = cursor.getString(3);
                if (inTime == null || inTime.isEmpty()) {
                    attendance.setInTime("-");
                } else {
                    attendance.setInTime(formatTime(inTime));
                }
                if (outTime == null || outTime.isEmpty()) {
                    attendance.setOutTime("-");
                } else {
                    attendance.setOutTime(formatTime(outTime));
                }
                attendance.setHours(cursor.getDouble(4));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return attendanceList;
    }

    @SuppressLint("SimpleDateFormat")
    private String formatTime(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
    }
    @SuppressLint("SimpleDateFormat")
    private String formatDate(String dateInput) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = inputFormat.parse(dateInput);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
    }


    public List<AttendanceModal> getAllAttendanceForEmployee(int employeeId) {
        List<AttendanceModal> attendanceList = new ArrayList<>();

        // Create the query
        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + ID_COL + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId)});

        // Loop through the results and create Attendance objects
        if (cursor.moveToFirst()) {
            do {
                AttendanceModal attendance = new AttendanceModal();
                attendance.setEmployeeId(Integer.parseInt(cursor.getString(0)));
                attendance.setDate(cursor.getString(1));
                attendance.setInTime(cursor.getString(2));
                attendance.setOutTime(cursor.getString(3));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return attendanceList;
    }

    public int getMissingAttendanceCountInCurrentMonth(int employeeId, String currentMonth, int dayToExclude) {
        int missingCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDay; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            if (calendar.get(Calendar.DAY_OF_WEEK) != dayToExclude) {
                String date = currentMonth + "-" + String.format("%02d", day);
                String query = "SELECT COUNT(*) FROM " + TABLE_ATTENDANCE +
                        " WHERE " + ID_COL + " = ?" +
                        " AND " + DATE_COL + " = ?";
                Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), date});
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    if (count == 0) {
                        missingCount++;
                    }
                }
                cursor.close();
            }
        }
        return missingCount;
    }

    public double getOvertimeInCurrentMonth(int employeeId, String currentMonth) {
        int overtime = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(CASE WHEN " + IN_TIME_COL + " = " + OUT_TIME_COL + " THEN 16 * 3600 WHEN strftime('%s', CASE WHEN " + OUT_TIME_COL + " < " + IN_TIME_COL + " THEN datetime(" + OUT_TIME_COL + ", '+1 day') ELSE " + OUT_TIME_COL + " END) - strftime('%s', " + IN_TIME_COL + ") <= 8 * 3600 THEN 0 ELSE strftime('%s', CASE WHEN " + OUT_TIME_COL + " < " + IN_TIME_COL + " THEN datetime(" + OUT_TIME_COL + ", '+1 day') ELSE " + OUT_TIME_COL + " END) - strftime('%s', " + IN_TIME_COL + ") - 8 * 3600 END)" +
                " FROM " + TABLE_ATTENDANCE +
                " WHERE " + ID_COL + " = ?" +
                " AND strftime('%Y-%m', " + DATE_COL + ") = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(employeeId), currentMonth});
        if (cursor.moveToFirst()) {
            overtime = cursor.getInt(0);
        }
        cursor.close();
        return Math.round((overtime / 3600.0) * 100.0) / 100.0;
    }

    public double getPaymentInCurrentMonth(int employeeId, String currentMonth) {
        double payment = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String salaryQuery = "SELECT " + SALARY_COL + " FROM " + TABLE_EMPLOYEE +
                " WHERE " + ID_COL + " = ?";
        Cursor salaryCursor = db.rawQuery(salaryQuery, new String[]{String.valueOf(employeeId)});
        if (salaryCursor.moveToFirst()) {
            double dailySalary = salaryCursor.getDouble(0) / 8;
            String attendanceQuery = "SELECT SUM(CASE WHEN " + IN_TIME_COL + " = " + OUT_TIME_COL + " THEN 24 * 3600 ELSE strftime('%s', CASE WHEN " + OUT_TIME_COL + " < " + IN_TIME_COL + " THEN datetime(" + DATE_COL + " || ' ' ||" +
                    OUT_TIME_COL+ ", '+1 day') ELSE datetime(" +
                    DATE_COL+  " || ' ' ||" +
                    OUT_TIME_COL+ ") END) - strftime('%s', datetime(" +
                    DATE_COL+  " || ' ' ||" +
                    IN_TIME_COL+ ")) END)" +
                    " FROM "+TABLE_ATTENDANCE+
                    " WHERE "+ID_COL+" = ?"+
                    " AND strftime('%Y-%m', "+DATE_COL+") = ?";
            Cursor attendanceCursor = db.rawQuery(attendanceQuery, new String[]{String.valueOf(employeeId), currentMonth});
            if (attendanceCursor.moveToFirst()) {
                double totalSecondsWorked = attendanceCursor.getDouble(0);
                payment = (totalSecondsWorked / 3600) * dailySalary;
            }
            attendanceCursor.close();
        }
        salaryCursor.close();
        return Math.round(payment * 100.0) / 100.0;
    }

    public double getTotalPaymentInCurrentMonth() {
        @SuppressLint("SimpleDateFormat")
        String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());

        double totalPayment = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID_COL + " FROM " + TABLE_EMPLOYEE;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int employeeId = cursor.getInt(0);
            double payment = getPaymentInCurrentMonth(employeeId, currentMonth);
            totalPayment += payment;
        }
        cursor.close();
        return Math.round(totalPayment * 100.0) / 100.0;
    }

    public double getHighestPaymentInCurrentMonth() {
        @SuppressLint("SimpleDateFormat")
        String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());

        double highestPayment = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID_COL + " FROM " + TABLE_EMPLOYEE;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int employeeId = cursor.getInt(0);
            double payment = getPaymentInCurrentMonth(employeeId, currentMonth);
            if (payment > highestPayment) {
                highestPayment = payment;
            }
        }
        cursor.close();
        return Math.round(highestPayment * 100.0) / 100.0;
    }

    public double getLowestPaymentInCurrentMonth() {
        @SuppressLint("SimpleDateFormat")
        String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());

        double lowestPayment = Double.MAX_VALUE;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + ID_COL + " FROM " + TABLE_EMPLOYEE;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int employeeId = cursor.getInt(0);
            double payment = getPaymentInCurrentMonth(employeeId, currentMonth);
            if (payment < lowestPayment) {
                lowestPayment = payment;
            }
        }
        cursor.close();
        // Check if there was no data in the database
        if (lowestPayment == Double.MAX_VALUE) {
            return 0.0;
        } else {
            return Math.round(lowestPayment * 100.0) / 100.0;
        }
    }

    public void deleteEmployee(int employeeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, ID_COL + " = ?",
                new String[] { String.valueOf(employeeId) });
        db.close();
    }
    public String addAttendance(int employeeId, String date, String inTime, String outTime) {
        // Check if employeeId and date are provided
        if (employeeId == 0 || date == null || date.isEmpty()) {
            return "Error: Employee ID and date must be provided";
        }

        // Trim leading and trailing white space from inTime and outTime
        if (inTime != null) {
            inTime = inTime.trim();
        }
        if (outTime != null) {
            outTime = outTime.trim();
        }

        // Check if there is already an attendance record for this employee and date
        Cursor cursor = getReadableDatabase().query(
                TABLE_ATTENDANCE,
                new String[] { ID_COL, IN_TIME_COL },
                ID_COL + "=? AND " + DATE_COL + "=?",
                new String[] { String.valueOf(employeeId), date },
                null, null, null
        );

        ContentValues values = new ContentValues();
        values.put(ID_COL, employeeId);
        values.put(DATE_COL, date);

        if (inTime != null && !inTime.isEmpty()) {
            values.put(IN_TIME_COL, inTime);
        } else if (outTime != null && !outTime.isEmpty()) {
            // inTime is empty and outTime is not empty
            if (cursor.moveToFirst()) {
                // There is already an attendance record for this employee and date
                String existingInTime = cursor.getString(1);
                if (existingInTime == null || existingInTime.isEmpty()) {
                    // The existing attendance record has an empty inTime value
                    cursor.close();
                    return "Error: No inTime value present for the given date";
                } else {
                    // The existing attendance record has a non-empty inTime value
                    values.put(OUT_TIME_COL, outTime);
                }
            } else {
                // There is no attendance record for this employee and date
                cursor.close();
                return "Error: No inTime value present for the given date";
            }
        }

        if (outTime != null && !outTime.isEmpty()) {
            values.put(OUT_TIME_COL, outTime);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        if (cursor.moveToFirst()) {
            // There is already an attendance record for this employee and date
            // Update the record with the new data
            db.update(
                    TABLE_ATTENDANCE,
                    values,
                    ID_COL + "=? AND " + DATE_COL + "=?",
                    new String[] { String.valueOf(employeeId), date }
            );
        } else {
            // There is no attendance record for this employee and date
            // Insert a new record
            db.insert(TABLE_ATTENDANCE, null, values);
        }

        cursor.close();
        db.close();
        return "Success";
    }


}