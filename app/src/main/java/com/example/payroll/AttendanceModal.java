package com.example.payroll;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;

public class AttendanceModal {
    private int employeeId;
    private String date;
    private String inTime;
    private String outTime;
    private double hours;

    public AttendanceModal() {

    }

    public AttendanceModal(int employeeId, String date, String inTime, String outTime, double hours) {
        this.employeeId = employeeId;
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.hours = hours;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}
