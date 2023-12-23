package com.example.knockitadminapp.Model

class RiderPaymentModel(
    var id: String,
    var bankAccountNumber : String,
    var bankHolderName: String,
    var bankName: String,
    var ifscCode: String,
    var payment: String,
    var riderId: String,
    var timeStamp: Long,
    var totalAmount: Long
) {
    constructor() : this("","","","","","","",1,1)
}