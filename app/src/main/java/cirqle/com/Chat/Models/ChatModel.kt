package cirqle.com.Chat.Models

import cirqle.com.Authentication.Models.UserModel

data class ChatModel(var users:ArrayList<UserModel>, var latestMessage:String) {
}