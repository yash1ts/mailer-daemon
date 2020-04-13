package com.mailerdaemon.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
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
        UserModel model=new UserModel();
        model.setName(user.getDisplayName());
        model.setUserId(user.getUid());
        model.setRejectedPost(false);
        model.setEmail(user.getEmail());
        FirebaseFirestore.getInstance().collection("user").document(user.getUid()).set(model);
        getSharedPreferences("MAIN",MODE_PRIVATE).edit().putString("uid",user.getUid()).apply();
        createNotificationChannel();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,30);
        editor.putLong(StringRes.TIME_NOTI,calendar.getTimeInMillis());
        editor.putString("Name",user.getDisplayName()).apply();
        if(user.getUid().equals(StringRes.ADMIN_ID))
            editor.putBoolean("Access",true).apply();
        else
            editor.putBoolean("Access",false).apply();

        startMain(user);

    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MailerDaemon";
            String description = "Remider of Attendance Manager";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("id123", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

