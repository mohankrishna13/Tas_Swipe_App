package com.mohankrishna.swipeapp.model.retrofit

import com.mohankrishna.swipeapp.model.dataClass.Products
import com.mohankrishna.swipeapp.model.dataClass.ResponseModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @GET("api/public/get")
    suspend fun getProducts():ArrayList<Products>
    @Multipart
    @POST("api/public/add")
    suspend fun addProducts(@Part("product_name") product_name:String,
                            @Part("product_type") product_type:String,
                            @Part("price") price:Double,
                            @Part("tax") tax:Double,
                            @Part files :List<MultipartBody.Part>
    ): Response<ResponseModel>
}