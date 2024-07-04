package cirqle.com.Utils

import cirqle.com.Account.Models.GetOrdersModel
import cirqle.com.Account.Models.ProfileUpdateResponse
import cirqle.com.Authentication.Models.FcmResponseModel
import cirqle.com.Authentication.Models.RegisterResponseModel
import cirqle.com.Authentication.Models.SendOtpModel
import cirqle.com.Authentication.Models.UserModel
import cirqle.com.BuyAndSell.Models.BuyAndSellPostModel
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.BuyAndSell.Models.PostResponseModel
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.Chat.Models.FetchChatResponseModel
import cirqle.com.Chat.Models.MessageResponseModel
import cirqle.com.CirqleStore.Models.*
import cirqle.com.DateSpark.Models.CheckUserModel
import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.DateSpark.Models.DateSparkUserModel
import cirqle.com.DateSpark.Models.GetUserResponseModel
import cirqle.com.LostAndFound.AddLostAndFoundPost
import cirqle.com.LostAndFound.Models.AddPostModel
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.SeniorCirqle.Models.*
import cirqle.com.TravelBuddy.Models.AddTripModel
import cirqle.com.TravelBuddy.Models.AddTripResponseModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("cirqle/register")
    fun register(@Body user:UserModel):Call<RegisterResponseModel>

    @GET("cirqle/sendotp")
    fun sendOtp(@Query("phone") phone:String):Call<SendOtpModel>

    @POST("cirqle/verifyotp")
    fun verifyOtp(@Query("otp") otp:String):Call<SendOtpModel>

    @FormUrlEncoded
    @POST("cirqle/login")
    fun login(@Field("phone") phone:String, @Field("password") password:String) : Call<RegisterResponseModel>

    @POST("buyandsell/post_ads")
    fun PostAds(@Body user:BuyAndSellPostModel):Call<PostResponseModel>

    @GET("buyandsell/get_ads")
    fun getAds(@Query ("collegeName") collegeName:String=""):Call<GetPostResponseModel>

    @FormUrlEncoded
    @POST("chat/")
    fun accessChat(@Field("userId") userId:String, @Field("postUserId") postUserId:String) : Call<ChatResponseModel>


    @GET("chat/")
    fun fetchChats(@Query ("userId") userId:String="") : Call<FetchChatResponseModel>

    @FormUrlEncoded
    @POST("message/send")
    fun sendMessage(@Field("content") content:String, @Field("chatId") chatId:String, @Field ("userId") userId:String) : Call<MessageResponseModel>


    @GET("message/get-messages/{chatId}")
    fun getallMessages(@Path("chatId") chatId:String) : Call<ArrayList<MessageResponseModel>>

    @POST("datespark/register_user")
    fun datespark_register(@Body user:DateSparkUserModel):Call<DateSparkResponseModel>

    @GET("datespark/get_user")
    fun datespark_get_user(@Query ("collegeName") collegeName:String="", @Query("gender") gender:String):Call<GetUserResponseModel>

    @GET("datespark/check_user")
    fun check_user(@Query ("user") user:String=""):Call<CheckUserModel>


    @POST("askdoubts/")
    fun add_doubts(@Body user:AddDoubtModel):Call<AddDoubtResponseModel>

    @GET("askdoubts/")
    fun get_doubts(@Query ("collegeName") collegeName:String="", @Query ("filter") filter:String, @Query("category") category:String):Call<GetDoubtsResponseModel>

    @DELETE("askdoubts/delete")
    fun delete_post(@Query ("postId") postId:String):Call<DeleteResponseModel>
    @PUT("askdoubts/vote")
    fun vote(@Query ("postId") postId:String,@Query ("userId") userId:String,@Query ("position") position:String,@Query ("prevPosition") prevPosition:String):Call<VoteResponseModel>

    @POST("askdoubts/comment")
    fun add_comment(@Body cmnt:AddCommentModel):Call<AddCommentResponseModel>

    @GET("askdoubts/comment")
    fun get_comments(@Query ("postId") postId:String):Call<GetCommentResponseModel>
    @PUT("askdoubts/like")
    fun like(@Query ("postId") postId:String,@Query ("userId") userId:String,@Query ("likeOrDislike") likeOrDislike:String):Call<AddDoubtResponseModel>



    @POST(" lostandfound/post")
    fun add_lost_found_post(@Body post:AddPostModel):Call<AddPostResponseModel>

    @GET(" lostandfound/get")
    fun get_lost_found_post(@Query ("collegeName") collegeName:String,@Query ("category") category:String):Call<ArrayList<AddPostResponseModel>>


    @POST(" travelbuddy/")
    fun add_trip(@Body post:AddTripModel):Call<AddTripResponseModel>

    @GET(" travelbuddy/")
    fun get_trip(@Query ("collegeName") collegeName:String):Call<ArrayList<AddTripResponseModel>>

    @PUT("cirqle/updatefcmtoken")
    fun update_fcm(@Query ("userId") userId:String, @Query ("fcmToken") fcmToken:String):Call<FcmResponseModel>

    @PUT("update/profilepic")
    fun update_profilePic(@Query ("userId") userId:String, @Query ("profilePic") profilePic:String):Call<ProfileUpdateResponse>

    @GET("cirqlestore/get_product")
    fun get_product(@Query ("collegeName") collegeName:String="", @Query("category") category:String):Call<GetProductModel>

    @POST("cirqlestore/add_to_cart")
    fun add_to_cart(@Query ("product") product:String, @Query("user") user:String,@Query("quantity") quantity:Int):Call<GetCartResponseModel>

    @GET("cirqlestore/get_user_cart")
    fun get_user_cart(@Query("user") user:String,@Query("product") product:String=""):Call<MutableList<GetUserCartResponseModel>>

    @PUT("cirqlestore/update_cart_quantity")
    fun update_cart_quantity(@Query("cartId") cartId:String, @Query("quantity") quantity:Int):Call<GetCartResponseModel>

    @POST("cirqlestore/place_order")
    fun place_order(@Body order:PlaceOrderModel):Call<PlaceOrderResponseModel>


    @GET("user/get_buy_sell")
    fun get_user_buy_sell(@Query ("owner") owner:String=""):Call<GetPostResponseModel>

    @GET("user/get_lost_found")
    fun get_user_lost_found(@Query ("user") user:String,@Query ("category") category:String):Call<ArrayList<AddPostResponseModel>>

    @GET("user/get_user_feeds")
    fun get_user_feed(@Query ("user") user:String, @Query("category") category:String):Call<GetDoubtsResponseModel>

    @GET("cirqlestore/get_order")
    fun get_user_orders(@Query ("userId") user:String=""):Call<ArrayList<GetOrdersModel>>

    @POST("cirqlestore/register_complaint")
    fun register_complaint(@Query ("userId") user:String,@Query("complaint") complaint:String):Call<String>



}