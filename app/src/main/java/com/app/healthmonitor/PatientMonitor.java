package com.app.healthmonitor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientMonitor extends AppCompatActivity {
    private int currentApiVersion;
    private ArrayList<UserProfile> dataList;
    RecyclerView recyclerView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_monitor);

        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PatientMonitor.this));
        CardView BB = findViewById(R.id.Back_B);
        dataList = new ArrayList<>();
        PatientAdapter patientAdapter = new PatientAdapter(PatientMonitor.this, dataList);
        recyclerView.setAdapter(patientAdapter);
        MainApplication.getApplication().getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    UserProfile userProfile = item.getValue(UserProfile.class);
                    dataList.add(userProfile);
                }
                patientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientMonitor.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        BB.setOnClickListener(view -> {
            this.finish();
        });
    }
}