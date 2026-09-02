package com.example.mvvmsampleapp.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mvvmsampleapp.data.db.entities.User
import com.example.mvvmsampleapp.data.repositories.UserRepository
import com.example.mvvmsampleapp.util.ApiException
import com.example.mvvmsampleapp.util.Coroutines
import com.example.mvvmsampleapp.util.NoInternetException

class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {

    var firstName: String? = null
    var lastName: String? = null
    var passwordConfirm: String? = null
    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun getLoggedInUser() = repository.getUser()
    fun onLogin(view: View){
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
    fun onLoginButtonClick(view: View){
        authListener?.onStarted()
        if(email.isNullOrEmpty() || password.isNullOrEmpty())
        {
            authListener?.onFailure("Invalid email or password")
            return
        }

        Coroutines.main {
           try {
               val authResponse= repository.userLogin(email!!, password!!)
               val user = User(
                   authResponse.id,
                   authResponse.username,
                   authResponse.email,
                   authResponse.firstName,
                   authResponse.lastName,
                   authResponse.gender,
                   authResponse.image,
                   authResponse.accessToken,
                   authResponse.refreshToken
               )
               repository.saveUser(user)
               authListener?.onSuccess(user)
           }catch (e: ApiException){
               authListener?.onFailure(e.message!!)
           }catch (e: NoInternetException){
               authListener?.onFailure(e.message!!)
           }
        }

    }

    fun onSignup(view: View){
        Intent(view.context, SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
    fun onSignupButtonClick(view: View){
        authListener?.onStarted()

        if (firstName.isNullOrEmpty()){
            authListener?.onFailure("Firstname is required")
            return
        }
        if (lastName.isNullOrEmpty()){
            authListener?.onFailure("Lastname is required")
            return
        }
        if (email.isNullOrEmpty()){
            authListener?.onFailure("Email is required")
            return
        }
        if (password.isNullOrEmpty()){
            authListener?.onFailure("Password is required")
            return
        }
        if (password != passwordConfirm){
            authListener?.onFailure("Password did not match")
            return
        }


        Coroutines.main {
            try {
                val signupResponse = repository.userSignup(firstName!!, lastName!!, email!!, password!!)
                val user = User(
                    signupResponse.id,
                    signupResponse.username,
                    signupResponse.email,
                    signupResponse.firstName,
                    signupResponse.lastName,
                    signupResponse.gender,
                    signupResponse.image,
                )
                repository.saveUser(user)
                authListener?.onSuccess(user)
            }catch (e: ApiException){
                authListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }

    }
}