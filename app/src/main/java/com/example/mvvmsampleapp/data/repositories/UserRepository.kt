package com.example.mvvmsampleapp.data.repositories

import com.example.mvvmsampleapp.data.network.MyApi
import com.example.mvvmsampleapp.data.network.SafeApiRequest
import com.example.mvvmsampleapp.data.network.responses.AuthResponse

class UserRepository: SafeApiRequest() {
    suspend fun userLogin(email: String, password: String): AuthResponse{
        return apiRequest { MyApi().userLogin(email, password) }
    }
}