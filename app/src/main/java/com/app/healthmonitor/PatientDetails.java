package com.app.healthmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PatientDetails extends AppCompatActivity {
    TextView name,age,height,weight,heartrate,bodytemp,bloodoxygen;
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
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

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        heartrate = findViewById(R.id.HeartRate);
        bodytemp = findViewById(R.id.bodyTemp);
        bloodoxygen = findViewById(R.id.BloodOxygen);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            name.setText(bundle.getString("name"));
            age.setText(bundle.getString("age"));
            height.setText(bundle.getString("height"));
            weight.setText(bundle.getString("weight"));
            heartrate.setText(bundle.getString("heartrate"));
            bodytemp.setText(bundle.getString("bodytemp"));
            bloodoxygen.setText(bundle.getString("bloodoxygen"));
        }
        CardView BB = findViewById(R.id.Back_B);
        BB.setOnClickListener(view -> {
            this.finish();
        });
    }
}