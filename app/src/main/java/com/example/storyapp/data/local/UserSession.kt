package com.example.storyapp.data.local

import android.content.Context

internal class UserSession(context: Context) {

    private val sessions = context.getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = sessions.edit()
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser() : UserModel {
        val model = UserModel()
        model.userId = sessions.getString(USER_ID, "")
        model.name = sessions.getString(NAME, "")
        model.token = sessions.getString(TOKEN, "")
        return model
    }

    companion object {
        private const val USER_SESSION = "user_session"
        private const val USER_ID = "user_id"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }
}