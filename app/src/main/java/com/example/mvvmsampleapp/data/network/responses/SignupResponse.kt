package com.example.mvvmsampleapp.data.network.responses

data class SignupResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
)