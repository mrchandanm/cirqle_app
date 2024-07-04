package cirqle.com.Chat.Models

data class FetchChatResponseModel(var success:Boolean, var message:String, var result:ArrayList<ChatResponseModel>) {
}