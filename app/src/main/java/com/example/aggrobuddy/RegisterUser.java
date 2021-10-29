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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button registerUser;
    private EditText editTextFullName, editTextEmailId, editTextPassword,editTextMobileNumber;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById (R.id.registerUser_Register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullname_registerUser);


        editTextEmailId = (EditText) findViewById(R.id.email_registerUser);


        editTextPassword = (EditText) findViewById(R.id.password1_registerUser);


        editTextMobileNumber = (EditText) findViewById(R.id.phone_registerUser);

        progressBar = (ProgressBar)  findViewById(R.id.progressBar_registerUser);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.registerUser_Register:
                registeruser();
                break;

        }

    }

    private void registeruser() {

        String email = editTextEmailId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String mobileNumber = editTextMobileNumber.getText().toString().trim();

        if (fullName.isEmpty())
        {
            editTextFullName.setError("Full Name is Required");
            editTextFullName.requestFocus();
            return;
        }
        if (mobileNumber.isEmpty())
        {
            editTextMobileNumber.setError("Mobile Number is Required");
            editTextMobileNumber.requestFocus();
            return;
        }
        if ((mobileNumber.length() < 9) || mobileNumber.length() > 11 )
        {
            editTextMobileNumber.setError("Enter Valid Mobile Number");
            editTextMobileNumber.requestFocus();
            return;
        }
        if (email.isEmpty())
        {
            editTextEmailId.setError("Please Enter Email");
            editTextEmailId.requestFocus();
            return;
        }
        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmailId.setError("Enter Correct Email id ");
            editTextEmailId.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() <6)
        {
            editTextPassword.setError("Min 6 Char Password Required");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            User user = new User(fullName,mobileNumber,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this,"User has been Register Sucessfully",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Intent i = new Intent(RegisterUser.this,LoginScreen.class);
                                            startActivity(i);
                                        }

                                        //


                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this,"Faield to register User Try Again....",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(RegisterUser.this,"Faield to register User Try Again....",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}