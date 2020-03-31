package com.riyas.whatsapp.data.cloud

import com.riyas.whatsapp.BuildConfig

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    private var mRestService: CloudService? = null

    fun getClient(url: String): CloudService? {
        if (mRestService == null) {
            val builder = OkHttpClient().newBuilder()
            builder.addInterceptor(FakeInterceptor())
            val client = builder.build()

            val retrofit = Retrofit.Builder()
                // Using custom Jackson Converter to parse JSON
                // Add dependencies:
                // com.squareup.retrofit:converter-jackson:2.0.0-beta2
                .addConverterFactory(GsonConverterFactory.create())
                // Endpoint
                .baseUrl(url)
                .client(client)
                .build()

            mRestService = retrofit.create(CloudService::class.java)
        }
        return mRestService
    }
}
