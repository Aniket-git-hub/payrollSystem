package com.example.payroll;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;

public class AttendanceModal {
    private int employeeId;
    private String date;
    private String intime;
    private String outtime;
    private double hours;

    public AttendanceModal(){

    }
    public AttendanceModal(int employeeId, String date, String intime, String outtime, double hours) {
        this.employeeId = employeeId;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.hours = hours;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getDate() throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(date);
        sdf.applyPattern("dd/MM/yy");
        return sdf.format(d);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntime() throws ParseException {
        if (intime.isEmpty()) {
            return "";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        Date date = inputFormat.parse(intime);
        return outputFormat.format(date);
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() throws ParseException {
        if (outtime.isEmpty()) {
            return "";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        Date date = inputFormat.parse(outtime);
        return outputFormat.format(date);
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public double getHours(){return hours;}

    public void setHours(double hours){this.hours = hours;}
}
