package cirqle.com.LostAndFound.Models

import android.os.Parcel
import android.os.Parcelable
import cirqle.com.Authentication.Models.UserModel

data class AddPostResponseModel(var category:String,var user:UserModel,var title:String,var description:String,var collegeName:String,var location:String,var date:String,var images:ArrayList<String> =ArrayList()):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(UserModel::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeParcelable(user, flags)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(collegeName)
        parcel.writeString(location)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddPostResponseModel> {
        override fun createFromParcel(parcel: Parcel): AddPostResponseModel {
            return AddPostResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<AddPostResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}
