package cirqle.com.BuyAndSell.Models

import org.bson.types.ObjectId

data class BuyAndSellPostModel(var price:Int,var title:String,var description:String,var category:String,var negotiable:String,var owner:String,val collegeName:String,var images:ArrayList<String>)
