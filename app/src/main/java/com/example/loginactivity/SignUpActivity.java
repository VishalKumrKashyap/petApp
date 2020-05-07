package com.example.loginactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText User_Email;
    private TextInputEditText UsereditName;
    private TextInputEditText User_Dob;
    private TextInputEditText User_password;
    private  TextInputEditText User_confirmpwd;
    private  TextInputEditText UserNumber;
    private FirebaseAuth mAuth;
    private ProgressDialog statusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User_Email = findViewById(R.id.User_Email);
        UsereditName = findViewById(R.id.UsereditName);
        User_Dob = findViewById(R.id.User_Dob);
        UserNumber = findViewById(R.id.UserName);
        User_password = findViewById(R.id.User_password);
        User_confirmpwd = findViewById(R.id.User_confirmpwd);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = User_Email.getText().toString();
                String username = UsereditName.getText().toString();
                String password = User_password.getText().toString();
                String cpassword = User_confirmpwd.getText().toString();
                if (cpassword.equals(password)) {
                    if (!email.isEmpty() && !username.isEmpty()) {
                        statusDialog = showDialog("registering user to cloud");
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
                    }
                }
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public ProgressDialog showDialog(String msg) {
        Context context;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public void hideDialog(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    private void updateUI(FirebaseUser currentUser) {
        hideDialog(statusDialog);
        if (currentUser != null) {
            // link to home
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        } else {
            Snackbar.make(User_Email,"user is not registered",Snackbar.LENGTH_LONG).show();
        }
    }
}