package com.example.mvvmsampleapp.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mvvmsampleapp.data.db.entities.User
import com.example.mvvmsampleapp.data.repositories.UserRepository
import com.example.mvvmsampleapp.util.Coroutines

class AuthViewModel: ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun onLoginButtonClick(view: View){
        authListener?.onStarted()
        if(email.isNullOrEmpty() || password.isNullOrEmpty())
        {
            authListener?.onFailure("Invalid email or password")
            return
        }

        Coroutines.main {
            val response = UserRepository().userLogin(email!!, password!!)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
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
                    authListener?.onSuccess(user)
                } else {
                    authListener?.onFailure("Login failed")
                }
            } else {
                authListener?.onFailure("Error Code: ${response.code()}")
            }
        }

    }
}