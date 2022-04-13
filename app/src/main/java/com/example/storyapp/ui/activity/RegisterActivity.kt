package com.example.storyapp.ui.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.pojo.Register
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.customview.EmailEditText
import com.example.storyapp.ui.customview.NameEditText
import com.example.storyapp.ui.customview.PasswordEditText
import com.example.storyapp.ui.customview.RegisterButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: RegisterButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var nameEditText: NameEditText

    private var correctEmail: Boolean = false
    private var correctPassword: Boolean = false
    private var correctName: Boolean = false

    private val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.registerButton
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword
        nameEditText = binding.etName

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length >= 2) {
                    correctName = true
                } else if (s.isNullOrBlank()) {
                    correctName = false
                } else if (!s.isNullOrBlank() && s.length < 2) {
                    correctName = false
                    nameEditText.error = "Name Minimum Length is 2"
                }
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

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
                } else !s.isNullOrEmpty()
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        registerButton.setOnClickListener {
            register()
        }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun setLoginButtonEnable() {
        registerButton.isEnabled = correctName && correctEmail && correctPassword
    }

    private fun register() {
        showLoading(true)
        val client = ApiConfig.getApiService().register(
            nameEditText.text.toString(),
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
        client.enqueue(object : Callback<Register> {
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        showLoading(false)
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        goLogin()
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    showLoading(false)
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
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

    private fun goLogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.putExtra("email", emailEditText.text.toString())
        i.putExtra("password", passwordEditText.text.toString())
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        showLoading(false)
        finish()
    }

}