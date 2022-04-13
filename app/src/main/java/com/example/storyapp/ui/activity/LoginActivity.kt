package com.example.storyapp.ui.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.pojo.Login
import com.example.storyapp.data.remote.pojo.LoginResult
import com.example.storyapp.ui.customview.EmailEditText
import com.example.storyapp.ui.customview.LoginButton
import com.example.storyapp.ui.customview.PasswordEditText
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.viewmodel.LoginViewModel
import com.example.storyapp.ui.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    private lateinit var loginViewModel: LoginViewModel

    private var correctEmail = false
    private var correctPassword = false

    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = binding.loginButton
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword

        val pref = UserSession.getInstance(dataStore)

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            emailEditText.setText(intent.getStringExtra("email"))
            correctEmail = true
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            passwordEditText.setText(intent.getStringExtra("password"))
            correctPassword = true
        }

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[LoginViewModel::class.java]

        setLoginButtonEnable()

        loginViewModel.getToken().observe(
            this
        ) { token: String ->
            if (token.isNotEmpty()) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
        }

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && emailRegex.matches(s.toString())) {
                    correctEmail = true
                } else if (!s.isNullOrEmpty() && !emailRegex.matches(s.toString())) {
                    emailEditText.error = "Invalid Email Address"
                    correctEmail = false
                } else {
                    correctEmail = false
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
                correctPassword = if (!s.isNullOrEmpty() && s.length < 6) {
                    passwordEditText.error = "Password Minimum Length is 6"
                    false
                } else {
                    true
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
        showLoading(true)
        val client = ApiConfig.getApiService()
            .login(emailEditText.text.toString(), passwordEditText.text.toString())
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        saveSession(responseBody.loginResult as LoginResult)
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun saveSession(loginResult: LoginResult) {
        loginViewModel.saveToken(loginResult.token as String)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        showLoading(false)
        finish()
    }
}