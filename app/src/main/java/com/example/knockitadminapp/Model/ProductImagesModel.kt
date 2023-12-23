package com.example.knockitadminapp.Model

class ProductImagesModel(
    var id: String,
    var image: String,
    var productId: String
) {
    constructor() : this("","", "")
}