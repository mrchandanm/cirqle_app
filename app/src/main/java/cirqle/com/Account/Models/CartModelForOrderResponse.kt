package cirqle.com.Account.Models

import cirqle.com.CirqleStore.Models.AddProductResponseModel

data class CartModelForOrderResponse(var _id:String, var product: AddProductResponseModel, var quantity:Int)
