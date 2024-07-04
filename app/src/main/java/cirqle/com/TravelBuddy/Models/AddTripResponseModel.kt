package cirqle.com.TravelBuddy.Models

import cirqle.com.Authentication.Models.UserModel

data class AddTripResponseModel(var to:String,var from:String,var user:UserModel,var description:String,var collegeName:String,var DepartureDate:String,var returnDate:String,var costSharing:String,var seatsAvailable:String)
