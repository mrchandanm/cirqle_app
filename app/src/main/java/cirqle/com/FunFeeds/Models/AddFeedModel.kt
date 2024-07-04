package cirqle.com.FunFeeds.Models

import android.os.Parcel
import android.os.Parcelable
import cirqle.com.SeniorCirqle.Models.OptionModel

data class AddFeedModel(var captain:String,var isAnonymos:String, var user:String, var collegeName:String, var options:ArrayList<OptionModel> = ArrayList(), var images:ArrayList<String>):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        ArrayList<OptionModel>().apply {
            parcel.readList(this as List<*>, OptionModel::class.java.classLoader)
        },
        ArrayList<String>().apply {
            parcel.readStringList(this)
        }
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(captain)
        parcel.writeString(isAnonymos)
        parcel.writeString(user)
        parcel.writeString(collegeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddFeedModel> {
        override fun createFromParcel(parcel: Parcel): AddFeedModel {
            return AddFeedModel(parcel)
        }

        override fun newArray(size: Int): Array<AddFeedModel?> {
            return arrayOfNulls(size)
        }
    }
}
