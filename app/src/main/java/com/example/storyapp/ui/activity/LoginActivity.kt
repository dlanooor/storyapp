package com.example.storyapp.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.storyapp.data.local.UserModel
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.pojo.Login
import com.example.storyapp.data.remote.pojo.LoginResult
import com.example.storyapp.ui.customview.EmailEditText
import com.example.storyapp.ui.customview.LoginButton
import com.example.storyapp.ui.customview.PasswordEditText
import com.example.storyapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    private var correctEmail: Boolean = false
    private var correctPassword: Boolean = false

    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = binding.loginButton
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            emailEditText.setText(intent.getStringExtra("email"))
            correctEmail = true
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            passwordEditText.setText(intent.getStringExtra("password"))
            correctPassword = true
        }

        setLoginButtonEnable()

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !emailRegex.matches(s.toString())) {
                    emailEditText.setError("Invalid Email Address")
                    correctEmail = false
                } else {
                    correctEmail = true
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 6) {
                    passwordEditText.setError("Password Minimum Length is 6")
                    correctPassword = false
                } else {
                    correctPassword = true
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        loginButton.setOnClickListener {
            login()
        }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    private fun setLoginButtonEnable() {
        loginButton.isEnabled = correctEmail && correctPassword
    }

    private fun login() {
        val client = ApiConfig.getApiService()
            .login(emailEditText.text.toString(), passwordEditText.text.toString())
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        saveSession(responseBody.loginResult as LoginResult)
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun saveSession(loginResult: LoginResult) {
        var userModel = UserModel(loginResult.userId, loginResult.name, loginResult.token)

        val i = Intent(this, MainActivity::class.java)
        i.putExtra(USER_SESSION, userModel)
        startActivity(i)
    }

    companion object {
        const val USER_SESSION = "user_session"
    }
}