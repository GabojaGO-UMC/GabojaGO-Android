package org.techtown.gabojago.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


fun setJwt(context: Context, name: String, jwt: String) {
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()
    editor.putString("jwt", jwt)
    editor.apply()
}

fun getJwt(context: Context, name: String): String{
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)

    return spf.getString("jwt", "")!!
}

val client: OkHttpClient = OkHttpClient.Builder()
    .readTimeout(30000, TimeUnit.MILLISECONDS)
    .connectTimeout(30000, TimeUnit.MILLISECONDS)
    .build()

fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://dev.gabojago.shop")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit
}
