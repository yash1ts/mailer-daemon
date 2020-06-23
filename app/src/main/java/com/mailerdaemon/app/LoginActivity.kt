package com.mailerdaemon.app;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity: AppCompatActivity() {

    private val EMAIL = "email"
    private val TAG = "LoginActivity"
    private val POSTS = "user_posts"
    private val RC_SIGN_IN = 234
    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar_NoStatusColor)
        val mAuth = FirebaseAuth.getInstance()
        if (getSharedPreferences("MAIN", Context.MODE_PRIVATE).getBoolean("intro", true)) {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        } else {
            val currentUser = mAuth.currentUser
            currentUser?.let { startMain(it) }
        }
        setContentView(R.layout.activity_login)
        progress_bar.visibility = View.GONE
        forgot_password.setOnClickListener { v: View? -> startActivity(Intent(this, ForgotPassActivity::class.java)) }
        login.setOnClickListener { v: View? ->
            val email = Objects.requireNonNull(login_email!!.text).toString().trim { it <= ' ' }
            val password = Objects.requireNonNull(login_password.text).toString()
            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    progress_bar.visibility = View.VISIBLE
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
                        progress_bar.visibility = View.GONE
                        if (task.isSuccessful) {
                            saveUser(Objects.requireNonNull(mAuth.currentUser))
                        } else {
                            Toast.makeText(applicationContext, "Invalid password", Toast.LENGTH_LONG).show()
                        }
                    }
                } else login_password.error = "Password cannot be empty"
            } else {
                login_email.error = "Email cannot be empty"
            }
        }
        signup.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        })
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                progress_bar.visibility = View.GONE
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                Toast.makeText(getApplicationContext(), "If you don't have a account please signup.$error", Toast.LENGTH_LONG).show()
            }
        })
        login_facebook.setOnClickListener { v: View? ->
            progress_bar.visibility = View.VISIBLE
            LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    Arrays.asList(EMAIL)
            )
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        google_signin.setOnClickListener { v: View? ->
            progress_bar.visibility = View.VISIBLE
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN)
        }

    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser!!
                        saveUser(user)
                    } else {
                        Toast.makeText(this@LoginActivity, "Authentication failed." + task.exception,
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun saveUser(user: FirebaseUser?) {
        val model = UserModel()
        model.name = user!!.displayName
        model.userId = user.uid
        model.rejectedPost = false
        model.email = user.email
        FirebaseFirestore.getInstance().collection("user").document(user.uid).set(model)
        getSharedPreferences("MAIN", Context.MODE_PRIVATE).edit().putString("uid", user.uid).apply()
        createNotificationChannel()
        val editor: SharedPreferences.Editor = getSharedPreferences("GENERAL", Context.MODE_PRIVATE).edit()
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 30
        editor.putLong(TIME_NOTI, calendar.timeInMillis)
        editor.putString("Name", user.displayName).apply()
        if (user.uid == ADMIN_ID) editor.putBoolean("Access", true).apply() else editor.putBoolean("Access", false).apply()
        startMain(user)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MailerDaemon"
            val description = "Remider of Attendance Manager"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("id123", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            progress_bar.visibility = View.GONE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth.currentUser!!
                        saveUser(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }


    private fun startMain(currentUser: FirebaseUser?) {
        intent = Intent(getApplicationContext(), MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
