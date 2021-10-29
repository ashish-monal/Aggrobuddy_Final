package com.example.aggrobuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressbar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        emailEditText = (EditText) findViewById(R.id.email_forget_password);
        resetPasswordButton = (Button) findViewById(R.id.resetPassword);
        progressbar = (ProgressBar) findViewById(R.id.progressbar_ForgetPassword);

        auth = FirebaseAuth.getInstance();
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {

        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty())
        {
            emailEditText.setError("Please Enter Email");
            emailEditText.requestFocus();
            return;
        }
        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Enter Correct Email id ");
            emailEditText.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(ForgetPassword.this, "Check Your registered mail to reset password....", Toast.LENGTH_LONG).show();

                progressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent i = new Intent(ForgetPassword.this,LoginScreen.class);
                        startActivity(i);
                    }
                }
                else
                {
                    Toast.makeText(ForgetPassword.this, "Try Again ! Something Wrong happened!...", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}