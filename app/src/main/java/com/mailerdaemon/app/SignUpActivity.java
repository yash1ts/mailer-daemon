package com.mailerdaemon.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import Utils.StringRes;


public class SignUpActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private FirebaseAuth mAuth;
    private TextInputEditText tvEmail, tvPassword;
    private String email, password;

    private void startMain(FirebaseUser currentUser) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvEmail = findViewById(R.id.login_email);
        tvPassword = findViewById(R.id.login_password);
        buttonSignUp=findViewById(R.id.signup);

        buttonSignUp.setOnClickListener(v -> {
            email= Objects.requireNonNull(tvEmail.getText()).toString().trim();
            password= Objects.requireNonNull(tvPassword.getText()).toString();
            if(!email.isEmpty())
            {
                if(!password.isEmpty())
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        saveUser(Objects.requireNonNull(mAuth.getCurrentUser()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error:"+task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
                else tvPassword.setError("Password cannot be empty");
            }else{
                tvEmail.setError("Email cannot be empty");
            }
        });

    }

    private void saveUser(FirebaseUser user) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("Name","Name").apply();
        if(user.getUid().equals(StringRes.ADMIN_ID))
            editor.putBoolean("Access",true).apply();
        else
            editor.putBoolean("Access",false).apply();

        startMain(user);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;

    }
}

