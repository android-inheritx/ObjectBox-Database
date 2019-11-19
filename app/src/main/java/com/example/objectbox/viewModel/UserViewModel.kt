package com.example.objectbox.viewModel

import io.objectbox.android.ObjectBoxLiveData
import androidx.lifecycle.ViewModel
import com.example.objectbox.database.User
import com.example.objectbox.database.User_
import io.objectbox.Box


class UserViewModel : ViewModel() {

    private var userLiveData: ObjectBoxLiveData<User>? = null

    fun getUserLiveData(userBox: Box<User>): ObjectBoxLiveData<User>? {
        if (userLiveData == null) {
            // query all notes, sorted a-z by their text
            userLiveData = ObjectBoxLiveData(userBox.query().order(User_.name).build())
        }
        return userLiveData
    }
}