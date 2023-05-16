package com.example.payroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeeRVAdapter extends RecyclerView.Adapter<EmployeeRVAdapter.ViewHolder> {

    // variable for our array list and context
    private ArrayList<EmployeeModal> EmployeeModalArrayList;
    private Context context;

    // constructor
    public EmployeeRVAdapter(ArrayList<EmployeeModal> EmployeeModalArrayList, Context context) {
        this.EmployeeModalArrayList = EmployeeModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting data
        // to our views of recycler view item.
        EmployeeModal modal = EmployeeModalArrayList.get(position);
//        holder.courseNameTV.setText(modal.getCourseName());
//        holder.courseDescTV.setText(modal.getCourseDescription());
//        holder.courseDurationTV.setText(modal.getCourseDuration());
//        holder.courseTracksTV.setText(modal.getCourseTracks());
        holder.employeeNameTV.setText(modal.getEmployeeName());
        holder.employeeContactTV.setText(modal.getEmployeeContact());
        holder.employeeSalaryTV.setText(String.valueOf(modal.getEmployeeSalary()));
        holder.employeeAddressTV.setText(modal.getEmployeeAddress());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return EmployeeModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView employeeNameTV, employeeContactTV, employeeSalaryTV, employeeAddressTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeNameTV = itemView.findViewById(R.id.employeeName);
            employeeContactTV = itemView.findViewById(R.id.employeeContact);
            employeeSalaryTV = itemView.findViewById(R.id.employeeSalary);
            employeeAddressTV = itemView.findViewById(R.id.employeeAddress);

        }
    }
}
