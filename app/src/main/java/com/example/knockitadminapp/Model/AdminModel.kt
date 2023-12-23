package com.example.knockitadminapp.Model

class AdminModel(
    var storeId: String,
    var name : String,
    var email: String,
    var profile: String,
    var number: String,
    var city: String,
    var state: String,
    var country: String,
    var pincode: String,
    var address: String,
    var latitude: Float,
    var longitude: Float,
    var yourStore: String,
    var timeStamp: Long
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        1F,
        1F,
        "",
        1
    )
}
