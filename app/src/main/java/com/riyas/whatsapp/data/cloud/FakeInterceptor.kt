package com.riyas.whatsapp.data.cloud

import android.os.Build
import android.util.Log

import com.google.gson.JsonObject
import com.riyas.whatsapp.BuildConfig
import com.riyas.whatsapp.model.UserDetails

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.net.URI
import java.util.Collections

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

/**
 *
 */
class FakeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        var response: Response? = null
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "---- DEBUG --- DEBUG -- DEBUG - DEBUG -- DEBUG --- DEBUG ----")
            Log.d(TAG, "----                FAKE SERVER RESPONSES                ----")
            var responseString = ""
            // Get Request URI.
            //     final URI uri = chain.request().uri();
            //    Log.d(TAG, "---- Request URL: " + uri.toString());
            // Get Query String.
            //   final String query = uri.getQuery();
            val body = chain.request().body()
            val data = bodyToString(body)
            Log.d(TAG, data)
            var successCode = 200
            try {
                val jsonObject = JSONObject(data)
                //final String[] parsedQuery = query.split("=");
                if (jsonObject.opt("password") .equals("Worldofplay@2020") && jsonObject.opt("username").equals("test@worldofplay.in")) {
                    responseString = RESPONSE_SUCCESS
                    successCode = 200
                } else if (jsonObject.has("password")) {
                    responseString = RESPONSE_FIALURE_400
                    successCode = 400
                } else {
                    responseString = RESPONSE_FIALURE_401
                    successCode = 401
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            // Parse the Query String.

            response = Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()

            Log.d(TAG, "---- DEBUG --- DEBUG -- DEBUG - DEBUG -- DEBUG --- DEBUG ----")
        } else {
            response = chain.proceed(chain.request())
        }

        return response
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            request!!.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }

    companion object {

        private val TAG = FakeInterceptor::class.java.simpleName

        // FAKE RESPONSES.


        internal val RESPONSE_SUCCESS = "{\"token\":\"VwvYXBpXC9\"}"
        internal val RESPONSE_FIALURE_400 = "{\"error\":\"invalid_credentials\",\n" +
                "\"description\": \"Email address and password is not a\n" +
                "valid combination.\"}"
        internal val RESPONSE_FIALURE_401 =
            "{\"error\":\"bad_request\", \"description\":\n" + "\"Network communication error.\"}"
    }
}