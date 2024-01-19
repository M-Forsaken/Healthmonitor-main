package com.app.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    View fragmentView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView Date = view.findViewById(R.id.Date_time);
        String Datetime = sdf.format(new Date());
        CardView HM,NH,PM;
        Date.setText(Datetime);
        HM = view.findViewById(R.id.HM_Card);
        NH = view.findViewById(R.id.NH_Card);
        PM = view.findViewById(R.id.PM_Card);
        HM.setOnClickListener(view_ -> {
            Intent intent = new Intent(getContext(), Monitor.class);
            startActivity(intent);
        });
        NH.setOnClickListener(view_ -> {
            Intent intent = new Intent(getContext(), Hospital.class);
            startActivity(intent);
        });
        PM.setOnClickListener(view_ -> {
            Intent intent = new Intent(getContext(), PatientMonitor.class);
            startActivity(intent);
        });
    }
}
