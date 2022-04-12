package com.example.storyapp.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
) : Parcelable