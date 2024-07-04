package cirqle.com.CirqleStore.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class AddProductResponseModel(
    var _id:String,
    var title:String,
    var productDetails:String,
    var price:Int,
    var mrp:Int,
    var minQuantity:Int,
    var category:String,
    var images: ArrayList<String>,
    var brand:String,
    var expiry:String, var noOfstock:Int,
    var collegeName:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.createStringArrayList()!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(productDetails)
        parcel.writeInt(price)
        parcel.writeInt(mrp)
        parcel.writeInt(minQuantity)
        parcel.writeString(category)
        parcel.writeStringList(images)
        parcel.writeString(brand)
        parcel.writeString(expiry)
        parcel.writeInt(noOfstock)
        parcel.writeString(collegeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddProductResponseModel> {
        override fun createFromParcel(parcel: Parcel): AddProductResponseModel {
            return AddProductResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<AddProductResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}
