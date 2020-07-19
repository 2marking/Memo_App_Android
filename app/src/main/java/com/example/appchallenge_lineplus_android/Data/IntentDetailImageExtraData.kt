package com.example.appchallenge_lineplus_android.Data

import android.os.Parcel
import android.os.Parcelable

class IntentDetailImageExtraData(var id: Long?, var title: String?, var mainText: String?, var imageType: String?, var thumnailImage:String?, var imagePath: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ){}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeString(title)
        parcel.writeString(mainText)
        parcel.writeString(imageType)
        parcel.writeString(thumnailImage)
        parcel.writeString(imagePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IntentDetailImageExtraData> {
        override fun createFromParcel(parcel: Parcel): IntentDetailImageExtraData {
            return IntentDetailImageExtraData(parcel)
        }

        override fun newArray(size: Int): Array<IntentDetailImageExtraData?> {
            return arrayOfNulls(size)
        }
    }
}
