package com.mohankrishna.swipeapp.model.dataClass
data class ResponseModel(
    val message: String,
    val product_details: AddProducts,
    val product_id: Int,
    val success: Boolean
)