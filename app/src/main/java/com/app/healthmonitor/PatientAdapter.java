package com.app.healthmonitor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<patientViewHolder> {
    private Context context;
    private List<UserProfile> DataList;

    public PatientAdapter(Context context, List<UserProfile> dataList) {
        this.context = context;
        this.DataList = dataList;
    }

    @NonNull
    @Override
    public patientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_recycler_item,parent,false);

        return new patientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull patientViewHolder holder, int position) {
        holder.Name.setText(DataList.get(position).name);
        holder.patientCard.setOnClickListener(view -> {
            Intent intent = new Intent(context,PatientDetails.class);
            intent.putExtra("name", DataList.get(holder.getAdapterPosition()).name);
            intent.putExtra("age", DataList.get(holder.getAdapterPosition()).age);
            intent.putExtra("height", DataList.get(holder.getAdapterPosition()).height);
            intent.putExtra("weight", DataList.get(holder.getAdapterPosition()).weight);
            intent.putExtra("bloodoxygen", DataList.get(holder.getAdapterPosition()).oxygen);
            intent.putExtra("bodytemp", DataList.get(holder.getAdapterPosition()).temperature);
            intent.putExtra("heartrate", DataList.get(holder.getAdapterPosition()).heartrate);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }
}
class patientViewHolder extends RecyclerView.ViewHolder{
    TextView Name;
    CardView Image, patientCard;
    public patientViewHolder(@NonNull View itemView) {
        super(itemView);
        Name = itemView.findViewById(R.id.patientName);
        Image = itemView.findViewById(R.id.patientImage);
        patientCard = itemView.findViewById(R.id.patientCard);
    }
}
