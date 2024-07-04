package cirqle.com.FunFeeds.Models

import cirqle.com.Authentication.Models.UserModel
import cirqle.com.SeniorCirqle.Models.OptionResponseModel

data class AddFeedResponseModel(var _id:String, var captain:String,var isAnonymos:String, var user: UserModel, var collegeName:String, var options:ArrayList<OptionResponseModel>, var images:ArrayList<String> =ArrayList())
