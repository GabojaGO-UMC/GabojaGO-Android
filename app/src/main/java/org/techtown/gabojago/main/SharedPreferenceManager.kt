package org.techtown.gabojago.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


fun setJwt(context: Context, name: String, jwt: String) {
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()
    editor.putString("jwt", jwt)
    editor.apply()
}

fun setBooleanJwt(context: Context, name: String, jwt: Boolean) {
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()
    editor.putBoolean("jwt", jwt)
    editor.apply()
}

fun getJwt(context: Context, name: String): String{
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)

    return spf.getString("jwt", "")!!
}

fun getBooleanJwt(context: Context, name: String): Boolean{
    val spf = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)

    return spf.getBoolean("jwt", false)!!
}

val client: OkHttpClient = OkHttpClient.Builder()
    .readTimeout(30000, TimeUnit.MILLISECONDS)
    .connectTimeout(30000, TimeUnit.MILLISECONDS)
    .build()

fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://prod.gabojago.shop")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit
}

var gson = GsonBuilder().setLenient().create()

fun withdrawalRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://nid.naver.com/oauth2.0/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    return retrofit
}