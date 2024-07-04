package cirqle.com.SeniorCirqle.Models

import cirqle.com.Authentication.Models.UserModel

data class AddDoubtResponseModel(var _id:String, var question:String,var category:String,var isAnonymos:String, var user:UserModel,var collegeName:String, var options:ArrayList<OptionResponseModel>, var images:ArrayList<String> =ArrayList(),var likes:ArrayList<String> =ArrayList())
