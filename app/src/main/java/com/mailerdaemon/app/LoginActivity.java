package com.mailerdaemon.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email",TAG="LoginActivity",POSTS="user_posts";
    private static final Integer RC_SIGN_IN=234;
    private Button buttonLogin,buttonSignUp;
    private ImageButton buttonFbLogin;
    private SignInButton buttonGoogleSignin;
    private FirebaseAuth mAuth;
    private TextInputEditText tvEmail, tvPassword;
    private String email, password;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView forgot_pass;
    private CardView progress;

    private void showUpdate() {

    }

    private void startMain(FirebaseUser currentUser) {

        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar_NoStatusColor);
        mAuth = FirebaseAuth.getInstance();
        if(getSharedPreferences("MAIN",MODE_PRIVATE).getBoolean("intro",true)) {
            startActivity(new Intent(this, IntroActivity.class));
            finish();
        }else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            FirebaseRemoteConfig mFirebaseRemoteConfig;
            if (currentUser != null) {
                startMain(currentUser);
            }
        }
        setContentView(R.layout.activity_login);

        tvEmail = findViewById(R.id.login_email);
        tvPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.login);
        buttonSignUp=findViewById(R.id.signup);
        progress=findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);
        buttonFbLogin=findViewById(R.id.login_facebook);
        buttonGoogleSignin=findViewById(R.id.google_signin);
        forgot_pass=findViewById(R.id.forgot_password);
        forgot_pass.setOnClickListener(v-> startActivity(new Intent(this,ForgotPassActivity.class)));

        buttonLogin.setOnClickListener(v -> {


            email= Objects.requireNonNull(tvEmail.getText()).toString().trim();
            password= Objects.requireNonNull(tvPassword.getText()).toString();
            if(!email.isEmpty())
            {   if(!password.isEmpty()) {
                progress.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progress.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        saveUser(Objects.requireNonNull(mAuth.getCurrentUser()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                    }
                });
            }
                else tvPassword.setError("Password cannot be empty");
            }
            else {
                tvEmail.setError("Email cannot be empty");
            }
        });
        buttonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        });
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress.setVisibility(View.GONE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "If you don't have a account please signup."+error, Toast.LENGTH_LONG).show();
            }
        });

        buttonFbLogin.setOnClickListener(v ->{
            progress.setVisibility(View.VISIBLE);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList(EMAIL));
                }
                );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
        buttonGoogleSignin.setOnClickListener(v -> {
            progress.setVisibility(View.VISIBLE);
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        saveUser(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed."+task.getException(),
                                Toast.LENGTH_SHORT).show();

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
        SharedPreferences.Editor editor = getSharedPreferences("GENERAL", Context.MODE_PRIVATE).edit();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,30);
        editor.putLong(ConstantsKt.TIME_NOTI,calendar.getTimeInMillis());
        editor.putString("Name",user.getDisplayName()).apply();
        if(user.getUid().equals(ConstantsKt.ADMIN_ID))
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            progress.setVisibility(View.GONE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        saveUser(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }


}

