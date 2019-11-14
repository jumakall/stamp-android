package com.stamp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stamp.model.LoggedInUser
import com.stamp.model.LoginCredentials
import com.stamp.model.Pass
import com.stamp.viewModel.StampViewModel
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// todo: add proper API URL
private const val BASE_URL = "https://localhost/"

private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(ApiAuthorizationInterceptor.getInstance())
    .build()

private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .client(client)
            .build()

interface StampApiService {

    @POST("auth/login")
    fun getLoginAsync(@Body body: LoginCredentials): Deferred<LoggedInUser>

    @GET("pass")
    fun getPassesAsync(): Deferred<List<Pass>>

    @GET("vendor/pass")
    fun getVendorPassesAsync(): Deferred<List<Pass>>

    @POST("stamp")
    fun postStampAsync(@Body body: StampViewModel.StampInformation): Deferred<ResponseBody>

}

object StampApi {
    val retrofitService : StampApiService by lazy { retrofit.create(StampApiService::class.java) }
}
