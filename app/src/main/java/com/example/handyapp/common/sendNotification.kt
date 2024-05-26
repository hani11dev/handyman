package com.example.handyapp.common

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun sendNotification(playerId: String, title: String, message: String) {
    val client = OkHttpClient()
    val json = JSONObject()
        .put("app_id", "c488fbb1-5a81-4c61-8ed9-13a60379241f")
        .put("include_player_ids", JSONArray().put(playerId))
        .put("contents", JSONObject().put("en", message))
        .put("headings", JSONObject().put("en", title))


    val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
    val request = okhttp3.Request.Builder()
        .url("https://onesignal.com/api/v1/notifications")
        .post(body)
        .addHeader("Authorization", "YjYwMThjZTItZTJmZS00NjkyLWJlM2MtZTIwNDljZGM3MmVl")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: okhttp3.Response) {
            println(response.body?.string())
        }
    })
}