package com.example.appointment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointment.Model.DoctorTeam;
import com.example.appointment.R;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<DoctorTeam> doctorList;

    public TeamAdapter(List<DoctorTeam> doctorList) {
        this.doctorList = doctorList;
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDoctor;
        TextView textName, textPosition;

        public TeamViewHolder(View itemView) {
            super(itemView);
            imageDoctor = itemView.findViewById(R.id.imageDoctor);
            textName = itemView.findViewById(R.id.textName);
            textPosition = itemView.findViewById(R.id.textPosition);
        }
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        DoctorTeam doctor = doctorList.get(position);
        holder.textName.setText(doctor.getName());
        holder.textPosition.setText(doctor.getTitle());
        holder.imageDoctor.setImageResource(doctor.getImageResId());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}

