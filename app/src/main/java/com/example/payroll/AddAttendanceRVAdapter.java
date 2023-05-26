package com.example.payroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.List;

public class AddAttendanceRVAdapter extends RecyclerView.Adapter<AddAttendanceRVAdapter.AddAttendanceRVViewHolder> {
    public List<EmployeeModal> employeeModalList;

    Context context;

    buttonClickListener buttonClickListener;

    public AddAttendanceRVAdapter(Context context, List<EmployeeModal> employeeModalList, buttonClickListener buttonClickListener) {
        this.context = context;
        this.employeeModalList = employeeModalList;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public AddAttendanceRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddAttendanceRVViewHolder(LayoutInflater.from(context).inflate(R.layout.add_attendance_item, parent, false),
                buttonClickListener
                );
    }

    @Override
    public void onBindViewHolder(@NonNull AddAttendanceRVViewHolder holder, int position) {
        holder.employeeNameTextView.setText(employeeModalList.get(position).getEmployeeName());
        holder.srno.setText("#" + (position + 1));

        try {
            if (employeeModalList.get(position).getInTime() != null) {
                holder.inTimeBtn.setEnabled(false);
                holder.inTimeBtn.setText(employeeModalList.get(position).getInTime());
            } else {
                holder.inTimeBtn.setEnabled(true);
                holder.inTimeBtn.setText("In");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            if (employeeModalList.get(position).getOutTime() != null) {
                holder.outTimeBtn.setEnabled(false);
                holder.outTimeBtn.setText(employeeModalList.get(position).getOutTime());
            } else {
                holder.outTimeBtn.setEnabled(true);
                holder.outTimeBtn.setText("Out");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public int getItemCount() {
        return employeeModalList.size();
    }

    public void updateData(List<EmployeeModal> employeeModalList) {
        this.employeeModalList = employeeModalList;
        notifyDataSetChanged();
    }

    public void updateButtonState(int employeeId, String type, String time) {
        for (int i = 0; i < employeeModalList.size(); i++) {
            if (employeeModalList.get(i).getEmployeeId() == employeeId) {
                if (type.equals("In")) {
                    employeeModalList.get(i).setInTime(time);
                } else {
                    employeeModalList.get(i).setOutTime(time);
                }
                notifyItemChanged(i);
                break;
            }
        }
    }

    class AddAttendanceRVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button inTimeBtn, outTimeBtn;
        TextView employeeNameTextView, srno;
        buttonClickListener buttonClickListener;

        public AddAttendanceRVViewHolder(@NonNull View itemView, buttonClickListener btnClickListener) {
            super(itemView);
            inTimeBtn = itemView.findViewById(R.id.employeeInTimeBtn);
            outTimeBtn = itemView.findViewById(R.id.employeeOutTimeBtn);
            employeeNameTextView = itemView.findViewById(R.id.employeeNameTextView);
            srno = itemView.findViewById(R.id.srno);
            buttonClickListener = btnClickListener;

            inTimeBtn.setOnClickListener(this);
            outTimeBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            buttonClickListener.onButtonClick(getAdapterPosition(), ((Button)v).getText().toString(), employeeModalList.get(getAdapterPosition()).getEmployeeId());
        }

    }

    interface buttonClickListener{
        void onButtonClick(int position, String type, int employeeId);
    }

}

