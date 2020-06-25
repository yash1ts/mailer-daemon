package com.mailerdaemon.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity: AppCompatActivity() {

    private val email = "email"
    private val rcsignin = 234
    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar_NoStatusColor)
        val mAuth = FirebaseAuth.getInstance()
        if (getSharedPreferences(MAIN, Context.MODE_PRIVATE).getBoolean("intro", true)) {
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        } else
            startMain()
        setContentView(R.layout.activity_login)
        progress_bar.visibility = View.GONE
        forgot_password.setOnClickListener { startActivity(Intent(this, ForgotPassActivity::class.java)) }
        login.setOnClickListener {
            val email = login_email.text.toString().trim { it <= ' ' }
            val password = (login_password.text).toString()
            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    progress_bar.visibility = View.VISIBLE
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
                        progress_bar.visibility = View.GONE
                        if (task.isSuccessful)
                            saveUser((mAuth.currentUser))
                         else
                            this.toast("Invalid password")
                    }
                } else login_password.error = "Password cannot be empty"
            } else login_email.error = "Email cannot be empty"
        }
        signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                progress_bar.visibility = View.GONE
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                this@LoginActivity.toast("If you don't have a account please signup.$error")
            }
        })
        login_facebook.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    listOf(email)
            )
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        google_signin.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            startActivityForResult(mGoogleSignInClient.signInIntent, rcsignin)
        }

    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful)
                        saveUser(mAuth.currentUser)
                    else
                        this.toast("Authentication failed." + task.exception)
                }
    }


    private fun saveUser(user: FirebaseUser?) {
        val model = UserModel(user?.uid, user?.displayName, user?.email,false)
        FirebaseFirestore.getInstance().collection(FB_USER).document(user!!.uid).set(model)
        getSharedPreferences(MAIN, Context.MODE_PRIVATE).edit().putString(U_ID, user.uid).apply()
        createNotificationChannel()
        val editor = getSharedPreferences(GENERAL, Context.MODE_PRIVATE).edit()
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 30
        editor.putLong(TIME_NOTI, calendar.timeInMillis)
        editor.putString("Name", user.displayName).apply()
        if (user.uid == ADMIN_ID) editor.putBoolean(ACCESS, true).apply()
        else editor.putBoolean(ACCESS, false).apply()
        startMain()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MailerDaemon"
            val description = "Remider of Attendance Manager"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcsignin) {
            progress_bar.visibility = View.GONE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                this.toast(e.message.toString())
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful)
                        saveUser( mAuth.currentUser)
                    else
                        // If sign in fails, display a message to the user.
                        this.toast("Authentication failed.")
                }
    }


    private fun startMain() {
        intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

