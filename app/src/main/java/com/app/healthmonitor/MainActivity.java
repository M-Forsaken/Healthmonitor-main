package com.app.healthmonitor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private int currentApiVersion;
    String State;
    Fragment Login, Signup;
    private ViewItemModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentApiVersion = Build.VERSION.SDK_INT;
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
        Login = new LoginFragment();
        Signup = new SignUpFragment();
        State = "Login";
        replaceFragment(Login);
        viewModel = new ViewModelProvider(this).get(ViewItemModel.class);
        viewModel.setAppState("Login");

        viewModel.getAppState().observe(this, item -> {
            switch (item){
                case "Login":
                    if (!Objects.equals(State, "Login")) {
                        State = "Login";
                        replaceFragment(Login);
                    }
                    break;
                case "SignUp":
                    if (!Objects.equals(State, "SignUp")) {
                        State = "SignUp";
                        replaceFragment(Signup);
                    }
                    break;
                default:
                    break;
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}