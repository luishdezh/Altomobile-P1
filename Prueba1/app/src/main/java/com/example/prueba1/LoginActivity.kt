package com.example.prueba1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import org.jetbrains.anko.doAsync
import java.util.*


class LoginActivity : AppCompatActivity() {

    var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private lateinit var loginButton: LoginButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton = findViewById(R.id.login_button)

            loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    LoginManager.getInstance().logInWithReadPermissions(
                        this@LoginActivity,
                        listOf("email")
                    )
                    var emailSet = AccessToken.getCurrentAccessToken().permissions.contains("email")
                    if(emailSet) {
                        startList()
                    }
                }

                override fun onCancel() {
                    val toast = Toast.makeText(this@LoginActivity, "You must accept", Toast.LENGTH_SHORT)
                    toast.show()
                    LoginManager.getInstance().logOut()
                }

                override fun onError(e: FacebookException) {
                    val toast = Toast.makeText(this@LoginActivity, "ERROR", Toast.LENGTH_SHORT)
                    toast.show()
                }
            })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        userStatus()
    }

    private fun userStatus() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn && AccessToken.getCurrentAccessToken().permissions.contains("email")) {
            startList()
        }
    }

    fun startList() {
        val intent = Intent( this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
