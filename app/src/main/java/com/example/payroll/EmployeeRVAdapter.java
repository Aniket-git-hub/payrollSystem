package com.example.payroll;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeeRVAdapter extends RecyclerView.Adapter<EmployeeRVAdapter.ViewHolder> {

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeeModal modal = EmployeeModalArrayList.get(position);

        holder.employeeNameTV.setText(modal.getEmployeeName());
        holder.employeeDesignationTV.setText(modal.getEmployeeDesignation());
    }

    @Override
    public int getItemCount() {
        return EmployeeModalArrayList.size();
    }
    public void updateData(ArrayList<EmployeeModal> employeeModalArrayList) {
        this.EmployeeModalArrayList = employeeModalArrayList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView employeeNameTV, employeeDesignationTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeNameTV = itemView.findViewById(R.id.employeeName);
            employeeDesignationTV = itemView.findViewById(R.id.employeeDesignation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, EmployeeDetails.class);
            EmployeeModal modal = EmployeeModalArrayList.get(position);
            intent.putExtra("id", String.valueOf(modal.getEmployeeId()));
            intent.putExtra("name", modal.getEmployeeName());
            intent.putExtra("age", String.valueOf(modal.getEmployeeAge()));
            intent.putExtra("address", modal.getEmployeeAddress());
            intent.putExtra("contact", modal.getEmployeeContact());
            intent.putExtra("email", modal.getEmployeeEmail());
            intent.putExtra("designation", modal.getEmployeeDesignation());
            intent.putExtra("salary", String.valueOf(modal.getEmployeeSalary()));
            context.startActivity(intent);

        }
    }
}
