package cirqle.com.BuyAndSell.Models

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import cirqle.com.Authentication.Models.UserModel
import org.bson.types.ObjectId

class BuySellresponseModel(var price:Int=0,var title:String="",var description:String="",var category:String="",var negotiable:String="No",var owner:UserModel,val collegeName:String,var images:ArrayList<String> =ArrayList() ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(UserModel::class.java.classLoader)!!,
        parcel.readString().toString()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(price)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(negotiable)
        parcel.writeParcelable(owner, flags)
        parcel.writeString(collegeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuySellresponseModel> {
        override fun createFromParcel(parcel: Parcel): BuySellresponseModel {
            return BuySellresponseModel(parcel)
        }

        override fun newArray(size: Int): Array<BuySellresponseModel?> {
            return arrayOfNulls(size)
        }
    }
}