package com.stamp.model

import com.google.android.gms.vision.barcode.Barcode

data class Card(
    val name: String,
    val code: String,
    val type: Barcode
)