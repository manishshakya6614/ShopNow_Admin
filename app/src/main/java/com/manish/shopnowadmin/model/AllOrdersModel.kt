package com.manish.shopnowadmin.model

data class AllOrdersModel(
    val name : String? = "",
    val orderId : String? = "",
    val userId : String? = "",
    val status : String? = "",
    val productId : String? = "",
    val price : String? = ""
)