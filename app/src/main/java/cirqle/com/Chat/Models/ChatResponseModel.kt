package cirqle.com.Chat.Models

import cirqle.com.Authentication.Models.UserModel

data class ChatResponseModel(var _id:String, var users:ArrayList<UserModel>, var latestMessage:MessageModel)
