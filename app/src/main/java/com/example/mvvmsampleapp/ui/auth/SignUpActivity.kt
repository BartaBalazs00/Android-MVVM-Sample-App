package com.example.mvvmsampleapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mvvmsampleapp.R
import com.example.mvvmsampleapp.data.db.entities.User
import com.example.mvvmsampleapp.databinding.ActivitySignUpBinding
import com.example.mvvmsampleapp.ui.home.HomeActivity
import com.example.mvvmsampleapp.util.ApiException
import com.example.mvvmsampleapp.util.NoInternetException
import com.example.mvvmsampleapp.util.hide
import com.example.mvvmsampleapp.util.show
import com.example.mvvmsampleapp.util.snackbar
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.getValue

class SignUpActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            if (user != null) {
                Intent(this, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
        binding.buttonSignUp.setOnClickListener {
            userSignup()
        }
        binding.textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun userSignup(){
        val firstName = binding.editTextFirstName.text.toString().trim()
        val lastName = binding.editTextLastName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

        if (firstName.isEmpty()) {
            binding.editTextFirstName.error = "First name is required"
            binding.editTextFirstName.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            binding.editTextLastName.error = "Last name is required"
            binding.editTextLastName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email is required"
            binding.editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = "Please enter a valid email address"
            binding.editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is required"
            binding.editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            binding.editTextPassword.error = "Password must be at least 6 characters long"
            binding.editTextPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Please confirm your password"
            binding.editTextConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Passwords do not match"
            binding.editTextConfirmPassword.requestFocus()
            return
        }

        lifecycleScope.launch {
            try {
                binding.progressBar.show()
                val authResponse = viewModel.userSignup(firstName, lastName, email, password)
                val user = User(
                    authResponse.id,
                    authResponse.username,
                    authResponse.email,
                    authResponse.firstName,
                    authResponse.lastName,
                    authResponse.gender,
                    authResponse.image
                )

                viewModel.saveLoggedInUser(user)
                binding.progressBar.hide()
            }
            catch (e: ApiException){
                binding.progressBar.hide()
                binding.signUp.snackbar(e.message!!)
                e.printStackTrace()
            } catch (e: NoInternetException){
                binding.progressBar.hide()
                binding.signUp.snackbar(e.message!!)
                e.printStackTrace()
            }
        }
    }
}