package com.example.sandglass

import android.os.Parcel
import android.os.Parcelable
import android.view.View.BaseSavedState

class SavedState : BaseSavedState {

    var currentPlayTime = 0L

    constructor(superState: Parcelable?) : super(superState)

    constructor(source: Parcel?) : super(source) {
        source?.apply {
            currentPlayTime = readLong()
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeLong(currentPlayTime)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
            override fun createFromParcel(p0: Parcel?): SavedState = SavedState(p0)
            override fun newArray(p0: Int): Array<SavedState?> = arrayOfNulls(p0)
        }
    }
}