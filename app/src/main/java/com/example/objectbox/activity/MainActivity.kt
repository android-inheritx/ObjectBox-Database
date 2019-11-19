package com.example.objectbox.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.objectbox.BR
import com.example.objectbox.MyApplication
import com.example.objectbox.R
import com.example.objectbox.database.User
import com.example.objectbox.database.User_
import com.example.objectbox.databinding.ItemUserBinding
import com.example.objectbox.utils.hide
import com.example.objectbox.utils.hideKeyboard
import com.example.objectbox.utils.orZero
import com.example.objectbox.utils.show
import com.example.objectbox.viewModel.UserViewModel
import com.github.nitrico.lastadapter.LastAdapter
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var boxStore: BoxStore? = null
    var userBox: Box<User>? = null
    var mUserList: ObservableList<User> = ObservableArrayList<User>()
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setListener()
        setAdapter()


    }

    private fun init() {
        boxStore = MyApplication.getApp()?.getBoxStore()
        userBox = boxStore?.boxFor(User::class.java)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        userViewModel.getUserLiveData(userBox!!)?.observe(this,
            Observer<List<User>> {
                mUserList.clear()
                mUserList.addAll(it)
            })

        recUser.layoutManager = LinearLayoutManager(this)

//        mUserList.clear()
//        mUserList.addAll(userBox?.all.orEmpty())
    }

    private fun setListener() {
        fabAddUser.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        ivSearch.setOnClickListener {
            ivSearch.hide()
            tvTitle.hide()

            etSearch.requestFocus()
            containerSearch.show()
        }

        ivClose.setOnClickListener {
            hideKeyboard()

            mUserList.clear()
            mUserList.addAll(userBox?.all.orEmpty())

            etSearch.setText("")
            containerSearch.hide()
            ivSearch.show()
            tvTitle.show()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length.orZero() > 0) {
                    val searchedUserList =
                        userBox?.query()?.contains(User_.name, p0.toString())?.build()?.find()
                    mUserList.clear()
                    mUserList.addAll(searchedUserList.orEmpty())
                } else {
                    mUserList.clear()
                    mUserList.addAll(userBox?.all.orEmpty())
                }
            }
        })
    }

    private fun setAdapter() {
        LastAdapter(mUserList, BR.item)
            .map<User, ItemUserBinding>(R.layout.item_user) {
                onLongClick {
                    val currentItem = it.binding.item

                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("Are you sure, you want to delete this user?")

                    alertDialog.setPositiveButton(
                        "Yes"
                    ) { dialogInterface, i ->
                        userBox?.remove(currentItem!!.id)
                    }

                    alertDialog.setNegativeButton("Cancel") { dialogInterface, i ->
                    }

                    alertDialog.show()
                }
            }

            .into(recUser)
    }
}
