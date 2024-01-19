package com.app.healthmonitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileFragment extends Fragment {
    View fragmentView;
    private FirebaseUser User;
    private DatabaseReference databaseReference;
    CardView SaveProfile;
    String userID;
    EditText emailText, ageText, heightText, weightText, usernameText;
    CheckBox Male,Female;
    String sex;
    private ViewItemModel viewItemModel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewItemModel = new ViewModelProvider(requireActivity()).get(ViewItemModel.class);
        emailText = fragmentView.findViewById(R.id.email_address);
        ageText = fragmentView.findViewById(R.id.age);
        heightText = fragmentView.findViewById(R.id.height);
        weightText = fragmentView.findViewById(R.id.weight);
        usernameText = fragmentView.findViewById(R.id.name);
        Male = fragmentView.findViewById(R.id.Male_box);
        Female = fragmentView.findViewById(R.id.Female_box);
        SaveProfile= fragmentView.findViewById(R.id.save_profile);
        Male.setOnClickListener(view_ -> {
            sex = "Male";
            Female.setChecked(false);
        });
        Female.setOnClickListener(view_ -> {
            sex = "Female";
            Male.setChecked(false);
        });
        SaveProfile.setOnClickListener(view_ -> saveProfile());
    }
    void saveProfile(){
        String email = emailText.getText().toString().trim();
        String age = ageText.getText().toString().trim();
        String weight = weightText.getText().toString().trim();
        String height = heightText.getText().toString().trim();
        String name = usernameText.getText().toString().trim();
        if (!email.isEmpty()&&!age.isEmpty()&&!weight.isEmpty()&&!height.isEmpty()&&!name.isEmpty()) {
            UserProfile userProfile = new UserProfile(name, email, age, height, weight, sex);
            User = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance("https://health-monitor-9b304-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("UserProfile");
            userID = User.getUid();
            databaseReference.child(userID).setValue(userProfile);
        }
        viewItemModel.setAppState("Profile");
    }
}