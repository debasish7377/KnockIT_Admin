package com.example.knockitadminapp.Model

class ProductSpecificationModel(
    var id: String,
    var brand: String,
    var value: String,
    var timeStamp: Long,
    var productId: String
) {
    constructor() : this("","","",1,"")
}