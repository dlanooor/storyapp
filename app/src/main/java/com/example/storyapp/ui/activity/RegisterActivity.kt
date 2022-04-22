package com.example.storyapp.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()

        registerButton = binding.registerButton
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword
        nameEditText = binding.etName

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    correctName = s.length >= 2
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
                correctEmail =
                    !s.isNullOrEmpty() && LoginActivity.emailRegex.matches(s.toString())
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = !(!s.isNullOrEmpty() && s.length < 6)
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

    private fun playAnimation() {
        val appLogo = ObjectAnimator.ofFloat(binding.ivApplogo, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val etName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val etPassword = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, ALPHA).setDuration(
            DURATION
        )
        val registerButton =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, ALPHA).setDuration(DURATION)
        val tvAccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, ALPHA).setDuration(
            DURATION
        )

        AnimatorSet().apply {
            playSequentially(
                appLogo,
                tvLogin,
                tvName,
                etName,
                tvEmail,
                etEmail,
                tvPassword,
                etPassword,
                registerButton,
                tvAccount
            )
            start()
        }
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
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        showLoading(false)
        finish()
    }

    companion object {
        private const val DURATION = 200L
        private const val ALPHA = 1f
    }

}