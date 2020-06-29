package com.mailerdaemon.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Button send=findViewById(R.id.send);
        TextInputEditText email=findViewById(R.id.email);
        send.setOnClickListener(v->{
            String s= Objects.requireNonNull(email.getText()).toString().trim();
            if(!s.isEmpty())
            auth.sendPasswordResetEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Email Sent",Toast.LENGTH_SHORT).show();
                    onBackPressed();}
                    else Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_SHORT).show();

                }
            });
            else email.setError("Email cannot be empty");
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;

    }
}
