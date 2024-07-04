package cirqle.com.Chat.Models

import cirqle.com.Authentication.Models.UserModel

data class MessageModel(var sender:UserModel, var content:String, var chat:String)
