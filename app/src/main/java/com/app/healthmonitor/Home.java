package com.app.healthmonitor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    private int currentApiVersion;
    String Status = "Home";
    Button DeviceFragment, HomeFragment,ProfileFragment;
    Fragment Home, Device, Profile,editProfileFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private ViewItemModel viewItemModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Home = new HomeFragment();
        Profile = new ProfileFragment();
        Device = new DeviceFragment();
        editProfileFragment = new EditProfileFragment();
        viewItemModel = new ViewModelProvider(Home.this).get(ViewItemModel.class);
        viewItemModel.setAppState("Home");
        currentApiVersion = Build.VERSION.SDK_INT;
        replaceFragment(Home);

        final int flags =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        MainApplication.getApplication().setDatabaseReference(FirebaseDatabase.getInstance("https://health-monitor-9b304-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("UserProfile"));
        DeviceFragment = findViewById(R.id.DeviceFragment);
        HomeFragment = findViewById(R.id.HomeFragment);
        ProfileFragment = findViewById(R.id.ProfileFragment);

        DeviceFragment.setOnClickListener(view -> {
            viewItemModel.setAppState("Device");
        });
        HomeFragment.setOnClickListener(view -> {
            viewItemModel.setAppState("Home");
        });
        ProfileFragment.setOnClickListener(view -> {
            viewItemModel.setAppState("Profile");
        });
        viewItemModel.getAppState().observe(this, item -> {
            switch (item){
                case "Home":
                    replaceFragment(Home);
                    break;
                case "Device":
                    replaceFragment(Device);
                    break;
                case "Profile":
                    replaceFragment(Profile);
                    break;
                case "EditProfile":
                    replaceFragment(editProfileFragment);
                    break;
                default:
                    break;
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}