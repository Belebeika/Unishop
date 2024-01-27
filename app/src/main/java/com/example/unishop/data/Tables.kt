package com.example.unishop.data

data class Toilet(
    var id: String? = null,
    var price: Int? = null,
    var lifespan: Int? = null,
    var imageUrl: String? = null,
    var description: String? = null
)

data class ToiletCategory(
    var toiletId: String? = null,
    var categoryname: String? = null
)