package cirqle.com.Authentication.Models

data class RegisterResponseModel(var success:Boolean, var message:String,var user:UserModel,var token:String)
