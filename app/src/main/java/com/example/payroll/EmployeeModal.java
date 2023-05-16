package com.example.payroll;

public class EmployeeModal {
    private String employeeName;
    private String employeeAddress;
    private String employeeContact;
    private int employeeSalary;

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

    public EmployeeModal(String employeeName,
                         String employeeAddress,
                         String employeeContact,
                         int employeeSalary){
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeContact = employeeContact;
        this.employeeSalary = employeeSalary;
    }
}



















