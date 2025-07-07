package com.example.appointment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointment.Model.Appointment;
import com.example.appointment.R;

import java.util.List;
import java.util.function.Consumer;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private Context context;
    private Consumer<Integer> onDelete;
    private Consumer<Appointment> onEdit;

    public AppointmentAdapter(Context context, List<Appointment> appointments,
                              Consumer<Integer> onDelete, Consumer<Appointment> onEdit) {
        this.context = context;
        this.appointments = appointments;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment app = appointments.get(position);
        holder.fullName.setText(app.fullname);
        holder.details.setText(
                "Giới tính: " + app.gender + "\n" +
                "Tuổi: " + app.age + "\n" +
                "Số điện thoại: " + app.phone + "\n" +
                "Ngày hẹn: " + app.appointmentDate + "\n" +
                "Triệu chứng: " + app.diseases);
        holder.doctor.setText("Bác sĩ: " + app.doctorName);
        holder.status.setText("Trạng thái: " + app.status);


        if ("Chờ duyệt".equalsIgnoreCase(app.status)) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> onEdit.accept(app));
            holder.btnDelete.setOnClickListener(v -> onDelete.accept(app.id));
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, details, doctor, status;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.txtFullName);
            details = itemView.findViewById(R.id.txtDetails);
            doctor = itemView.findViewById(R.id.txtDoctor);
            status = itemView.findViewById(R.id.txtStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

