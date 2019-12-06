package com.example.prueba1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.pm.PackageManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import android.content.Intent
import android.widget.Toast
import com.facebook.login.widget.LoginButton


class LoginActivity : AppCompatActivity() {

    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                    loginButton.setPermissions((listOf("email")))
                    val intent = Intent( this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

            }

            override fun onCancel() {
                val toast = Toast.makeText(this@LoginActivity, "Debes de aceptar", Toast.LENGTH_SHORT)
                toast.show()

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

}
