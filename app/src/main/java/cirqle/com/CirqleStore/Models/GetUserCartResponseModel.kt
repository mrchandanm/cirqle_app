package cirqle.com.CirqleStore.Models

import android.os.Parcel
import android.os.Parcelable
import cirqle.com.Authentication.Models.UserModel

data class GetUserCartResponseModel(var _id:String,var product:AddProductResponseModel, var user:UserModel, var quantity:Int, var status:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(AddProductResponseModel::class.java.classLoader)!!,
        parcel.readParcelable(UserModel::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeParcelable(product, flags)
        parcel.writeParcelable(user, flags)
        parcel.writeInt(quantity)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetUserCartResponseModel> {
        override fun createFromParcel(parcel: Parcel): GetUserCartResponseModel {
            return GetUserCartResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<GetUserCartResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}
