package cirqle.com.SeniorCirqle.Models

import cirqle.com.Authentication.Models.UserModel

data class AddCommentResponseModel(var postId:String, var user:UserModel, var comment:String)
