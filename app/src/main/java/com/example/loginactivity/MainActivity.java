package com.example.loginactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText UserName;
    private TextInputEditText Userpwd;
    private FirebaseAuth mAuth;
    private ProgressDialog statusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        Button LoginButton = findViewById(R.id.LoginButton);
        Button SignUpButton = findViewById(R.id.SignUp_button);
        UserName = findViewById(R.id.UserName);
        Userpwd = findViewById(R.id.Userpwd);
        TextView ForgotPwd = findViewById(R.id.ForgotPwd);
        // connecting To firebase

        mAuth = FirebaseAuth.getInstance();
        LoginButton.setOnClickListener(this);
        SignUpButton.setOnClickListener(this);
        ForgotPwd.setOnClickListener(this);

    }


    public boolean onCreateOptionMenu(Menu menu) {
        //inflate the menu,
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        hideDialog(statusDialog);
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            // error msg
        }
    }

           @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginButton:
                processLogin();
                break;
            case R.id.SignUp_button:
                gotoRegister();
                break;
            case R.id.ForgotPwd:
                gotoForgot();
                break;
        }
    }

    private void gotoForgot() {
//        startActivity(new Intent(this,ForgotActivity.class));
    }

    private void gotoRegister() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void processLogin() {

        String email = UserName.getText().toString();
        String password = Userpwd.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(UserName, "email or password cannot be empty", Snackbar.LENGTH_INDEFINITE).show();
        } else if (email.length() < 10 || password.length() < 8) {
            Snackbar.make(UserName, "email or password length invalid", Snackbar.LENGTH_INDEFINITE).show();
        } else {
            statusDialog = showDialog("authenticating with server");
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        updateUI(null);
                    }
                }
            });
        }
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

}
