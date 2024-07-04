package cirqle.com.DateSpark.Models

import cirqle.com.Authentication.Models.UserModel

data class DateSparkResponseModel(var user:UserModel, var relationshipType:String,var gender:String,var collegeName:String,var images:ArrayList<String>)

