package com.example.knockitadminapp.Model

class BranchPaymentModel(
    var id: String,
    var bankAccountNumber : String,
    var bankHolderName: String,
    var bankName: String,
    var ifscCode: String,
    var payment: String,
    var branchId: String,
    var timeStamp: Long,
    var totalAmount: Long
) {
    constructor() : this("","","","","","","",1,1)
}