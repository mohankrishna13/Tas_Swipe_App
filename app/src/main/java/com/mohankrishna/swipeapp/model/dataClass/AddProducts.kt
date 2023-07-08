package com.mohankrishna.swipeapp.model.dataClass

import com.google.gson.annotations.SerializedName

data class AddProducts(
    @SerializedName("product_name")var product_name:String,
    @SerializedName("product_type")var product_type:String,
    @SerializedName("price")var price:Double,
    @SerializedName("tax")var tax:Double,
    )
