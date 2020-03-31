package com.riyas.whatsapp.data.cloud

import android.content.Context
import com.riyas.whatsapp.BuildConfig
import com.riyas.whatsapp.model.LoginResponse
import com.riyas.whatsapp.model.StoryResponse
import com.riyas.whatsapp.model.UserDetails
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CloudService {

    @POST("/login")
    suspend  fun login(@Body user:UserDetails): LoginResponse
    @GET("item/{id}.json")
    suspend fun getStory1(@Path(value = "id", encoded = true) id: String?): StoryResponse

    companion object Factory {

        fun create(context: Context): CloudService? {
            var url = BuildConfig.WEB_SERVICE_BASE_URL

            val retrofit =RestClient.getClient(BuildConfig.WEB_SERVICE_BASE_URL)
            return retrofit
        }
    }
}