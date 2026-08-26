package com.example.mvvmsampleapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var id: Int? = null,
    var username: String? =null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var gender: String? = null,
    var image: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}