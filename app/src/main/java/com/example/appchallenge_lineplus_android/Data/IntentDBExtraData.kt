package com.example.appchallenge_lineplus_android.Data

import android.os.Parcel
import android.os.Parcelable

class IntentDBExtraData(var id: Int?, var title: String?, var mainText: String?, var thumnailImage:String?, var imagePath: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ){}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id!!)
        parcel.writeString(title)
        parcel.writeString(mainText)
        parcel.writeString(thumnailImage)
        parcel.writeString(imagePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IntentDBExtraData> {
        override fun createFromParcel(parcel: Parcel): IntentDBExtraData {
            return IntentDBExtraData(parcel)
        }

        override fun newArray(size: Int): Array<IntentDBExtraData?> {
            return arrayOfNulls(size)
        }
    }
}
