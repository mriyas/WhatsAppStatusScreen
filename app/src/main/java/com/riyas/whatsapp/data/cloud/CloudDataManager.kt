package com.riyas.whatsapp.data.cloud

import android.content.Context
import com.riyas.whatsapp.model.LoginResponse
import com.riyas.whatsapp.model.UserDetails
import okhttp3.Response

class CloudDataManager (var context: Context){
    val service = CloudService.create(context)

    val TAG=javaClass.simpleName


    suspend fun login(details:UserDetails): LoginResponse? {
        //service?.getStory1("1")
        return service?.login(details)
    }
}