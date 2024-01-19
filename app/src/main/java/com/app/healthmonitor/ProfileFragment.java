package com.app.healthmonitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    View fragmentView;
    FragmentManager fragmentManager;
    String userID;
    private TextView emailText, ageText, heightText, weightText, sexText, usernameText;
    CardView Info, EditProfile, LogOut;
    UserProfile userProfile;
    private ViewItemModel viewItemModel;
    private final int currentApiVersion =  Build.VERSION.SDK_INT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        viewItemModel = new ViewModelProvider(requireActivity()).get(ViewItemModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        Info = fragmentView.findViewById(R.id.info);
        EditProfile = fragmentView.findViewById(R.id.edit_profile);
        LogOut = fragmentView.findViewById(R.id.LogOut);
        emailText = fragmentView.findViewById(R.id.email_address);
        ageText = fragmentView.findViewById(R.id.age);
        heightText = fragmentView.findViewById(R.id.height);
        weightText = fragmentView.findViewById(R.id.weight);
        sexText = fragmentView.findViewById(R.id.sex);
        usernameText = fragmentView.findViewById(R.id.username);
        userID = MainApplication.getApplication().getCurrentUser().getUid();
        MainApplication.getApplication().getDatabaseReference().child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(UserProfile.class);
                setupUserInfo(userProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireActivity(),"Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Info.setOnClickListener(this::showPopup);
        LogOut.setOnClickListener(view_ -> {
            startActivity(new Intent(requireActivity(),MainActivity.class));
            MainApplication.getApplication().setBluetoothService(null);
            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
        });
        EditProfile.setOnClickListener(view1 -> {
            viewItemModel.setAppState("EditProfile");
        });
    }
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.About_me:
                        Intent intent = new Intent(getContext(), About.class);
                        startActivity(intent);
                        return true;
                    case R.id.Contact:
                        sendEmail();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu);
        popup.show();
    }
    public void sendEmail(){
        String[] To_emails = {"20021554@vnu.edu.vn"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, To_emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android version: " + currentApiVersion);
        startActivity(Intent.createChooser(intent, "Choose an application"));
    }
    void setupUserInfo(UserProfile userProfile){
        if (userProfile != null){
            String name = userProfile.name;
            String email = userProfile.email;
            String age = userProfile.age;
            String height = userProfile.height;
            String weight = userProfile.weight;
            String sex = userProfile.sex;

            usernameText.setText(name);
            ageText.setText(age);
            heightText.setText(height);
            weightText.setText(weight);
            emailText.setText(email);
            sexText.setText(sex);
        }
    }
}