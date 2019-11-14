package com.stamp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pass (
    val id: Int,
    val name: String,
    val stamps: Int?,
    val max_stamps: Int
) : Parcelable {
    override fun toString(): String {
        return "$name ($max_stamps stamps)"
    }
}