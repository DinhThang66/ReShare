package com.example.reshare.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val profilePic: String,
    val latitude: Double?,
    val longitude: Double?
): Parcelable
