package com.example.mvvmsampleapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmsampleapp.data.network.MyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    fun userLogin(email: String, password: String): LiveData<String>{

        val loginResponse = MutableLiveData<String>()

        MyApi().userLogin(email, password)
            .enqueue(object: Callback<ResponseBody>{
                override fun onFailure(p0: Call<ResponseBody?>, p1: Throwable) {
                    loginResponse.value = p1.message
                }
                override fun onResponse(p0: Call<ResponseBody?>, p1: Response<ResponseBody?>) {
                    if (p1.isSuccessful) {
                        loginResponse.value = p1.body()?.string()
                    } else {
                        loginResponse.value = p1.errorBody()?.string()
                    }
                }
            })

        return loginResponse
    }

}