package com.app.healthmonitor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginFragment extends Fragment {
    View fragmentView;
    Boolean Visibility = false;
    TextView ForgotPassword,SignUp;
    EditText EmailText,PasswordText;
    ImageView password_visibility;
    CardView Login;
    private ViewItemModel viewModel;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewItemModel.class);
        return fragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        ForgotPassword = fragmentView.findViewById(R.id.forgot_password);
        SignUp = fragmentView.findViewById(R.id.SignUp_text);
        EmailText = fragmentView.findViewById(R.id.email_address);
        PasswordText = fragmentView.findViewById(R.id.password);
        Login = fragmentView.findViewById(R.id.login_button);
        password_visibility = fragmentView.findViewById(R.id.password_visible);
        password_visibility.setOnClickListener(view_ -> {
            if(Visibility) {
                PasswordText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                Visibility = false;
            }
            else {
                PasswordText.setInputType(InputType.TYPE_CLASS_TEXT);
                Visibility = true;
            }
        });
        SignUp.setOnClickListener(view_ -> {
            viewModel.setAppState("SignUp");
        });
        Login.setOnClickListener(view_ -> getAuth());
    }
    private void getAuth(){
        String email = EmailText.getText().toString().trim();
        String password = PasswordText.getText().toString().trim();
        if (email.isEmpty()){
            EmailText.setError("Email is Required.");
            EmailText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailText.setError("Please enter a valid email.");
            EmailText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            PasswordText.setError("Password is Required.");
            PasswordText.requestFocus();
            return;
        }
        if (password.length() < 6 || password.length() > 20){
            PasswordText.setError("Password length has to be from 6-20 characters.");
            PasswordText.requestFocus();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user!= null) {
                        if (user.isEmailVerified()) {
                            startActivity(new Intent(requireActivity(), Home.class));
                            MainApplication.getApplication().setCurrentUser(user);
                            requireActivity().finish();
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(requireActivity(),"Please verify your email account.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(requireActivity(),"Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}