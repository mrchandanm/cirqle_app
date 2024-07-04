package cirqle.com.CirqleStore.Models

import android.os.Parcel
import android.os.Parcelable
import cirqle.com.Authentication.Models.UserModel

data class CartResponseForPlaceOrder(var _id:String, var product:AddProductResponseModel, var user: String, var quantity:Int, var status:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(AddProductResponseModel::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeParcelable(product, flags)
        parcel.writeString(user)
        parcel.writeInt(quantity)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartResponseForPlaceOrder> {
        override fun createFromParcel(parcel: Parcel): CartResponseForPlaceOrder {
            return CartResponseForPlaceOrder(parcel)
        }

        override fun newArray(size: Int): Array<CartResponseForPlaceOrder?> {
            return arrayOfNulls(size)
        }
    }
}
