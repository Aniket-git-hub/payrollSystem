package com.example.payroll;

import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;

public class EmployeeModal {
    private int employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeContact;
    private int employeeSalary;
    private String employeeDesignation;
    private String employeeEmail;
    private int employeeAge;
    private String inTime;
    private String outTime;

    private String employeeJoiningDate;

    public String getEmployeeJoiningDate() {
        return employeeJoiningDate;
    }

    public void setEmployeeJoiningDate(String employeeJoiningDate) {
        this.employeeJoiningDate = employeeJoiningDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName(){
        return employeeName;
    }

    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress(){
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress){
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeContact(){
        return employeeContact;
    }

    public void setEmployeeContact(String employeeContact){
        this.employeeContact = employeeContact;
    }

    public int getEmployeeSalary(){
        return employeeSalary;
    }

    public void setEmployeeSalary(int employeeSalary){
        this.employeeSalary = employeeSalary;
    }

    public int getEmployeeAge(){
        return employeeAge;
    }
    public void setEmployeeAge(int age){
        this.employeeAge = age;
    }

    public String getEmployeeDesignation(){
        return employeeDesignation;
    }
    public void setEmployeeDesignation(String designation){
        this.employeeDesignation = designation;
    }

    public String getEmployeeEmail(){
        return employeeEmail;
    }

    public void setEmployeeEmail(String Email){
        this.employeeEmail = Email;
    }

    public EmployeeModal(int employeeId, String employeeName,
                         int employeeAge,
                         String employeeAddress,
                         String employeeContact,
                         String employeeEmail,
                         String employeeDesignation,
                         int employeeSalary, String employeeJoiningDate
                         ){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeContact = employeeContact;
        this.employeeSalary = employeeSalary;
        this.employeeAge = employeeAge;
        this.employeeEmail = employeeEmail;
        this.employeeDesignation = employeeDesignation;
        this.employeeJoiningDate = employeeJoiningDate;

    }

    public void setInTime(String time) {
        this.inTime = time;
    }
    public void setOutTime(String time) {
        this.outTime = time;
    }

    public String getInTime() throws ParseException {
        return inTime;
    }

    public String getOutTime() throws ParseException {
        return outTime;
    }
}



















