package cirqle.com.Authentication.Models

import android.os.Parcel
import android.os.Parcelable

data class UserModel(var _id:String="",var name:String, var userName:String,var phone:String, var password:String,var email:String, var gender:String,
                    var collegeName:String, var hostelName:String,var degree:String, var department:String,var passoutYear:String, var profilePic:String, var fcmToken:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
        parcel.writeString(userName)
        parcel.writeString(phone)
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeString(gender)
        parcel.writeString(collegeName)
        parcel.writeString(hostelName)
        parcel.writeString(degree)
        parcel.writeString(department)
        parcel.writeString(passoutYear)
        parcel.writeString(profilePic)
        parcel.writeString(fcmToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}
