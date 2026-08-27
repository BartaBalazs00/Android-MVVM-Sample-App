package com.example.mvvmsampleapp.ui.auth

import androidx.lifecycle.LiveData
import com.example.mvvmsampleapp.data.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
}