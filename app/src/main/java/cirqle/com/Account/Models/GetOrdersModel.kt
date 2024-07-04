package cirqle.com.Account.Models

import cirqle.com.Authentication.Models.UserModel
import java.util.*
import kotlin.collections.ArrayList

data class GetOrdersModel(var updatedAt: Date, var _id:String, var user: UserModel, var hostel:String, var status:String, var totalAmount:Int, var totalMrp:String, var carts:ArrayList<CartModelForOrderResponse>)
