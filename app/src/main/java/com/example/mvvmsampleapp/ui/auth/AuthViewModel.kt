package com.example.mvvmsampleapp.ui.auth

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

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun getLoggedInUser() = repository.getUser()
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
}