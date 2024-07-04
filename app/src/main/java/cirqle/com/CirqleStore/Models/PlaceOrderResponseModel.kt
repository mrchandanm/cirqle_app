package cirqle.com.CirqleStore.Models

import android.os.Parcel
import android.os.Parcelable
import cirqle.com.Authentication.Models.UserModel

data class PlaceOrderResponseModel(var _id:String,var user:UserModel,var hostel:String,var totalAmount:Int,var totalMrp:Int,var collegeName:String,var carts:ArrayList<CartResponseForPlaceOrder>):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(UserModel::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        ArrayList<CartResponseForPlaceOrder>().apply {
            parcel.readList(this, CartResponseForPlaceOrder::class.java.classLoader)
        }
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeParcelable(user, flags)
        parcel.writeString(hostel)
        parcel.writeInt(totalAmount)
        parcel.writeInt(totalMrp)
        parcel.writeString(collegeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceOrderResponseModel> {
        override fun createFromParcel(parcel: Parcel): PlaceOrderResponseModel {
            return PlaceOrderResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<PlaceOrderResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}
