package com.example.objectbox.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.objectbox.MyApplication
import com.example.objectbox.R
import com.example.objectbox.database.User
import com.example.objectbox.utils.isEmail
import com.example.objectbox.utils.orFalse
import com.example.objectbox.utils.orZero
import com.example.objectbox.utils.toastNow
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private var boxStore: BoxStore? = null
    var userBox: Box<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Handler().postDelayed({
            boxStore = MyApplication.getApp()?.getBoxStore()
            userBox = boxStore?.boxFor(User::class.java)

            setListener()
        }, 3000)

    }

    private fun setListener() {
        tvLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnSignUp.setOnClickListener {
            if (isValid()) {
                val put = userBox?.put(
                    User(
                        name = etName.text?.trim().toString(),
                        email = etEmail.text?.trim().toString(),
                        contactNumber = etContactNumber.text?.trim().toString().toLong()
                    )
                )

                etName.setText("")
                etEmail.setText("")
                etContactNumber.setText("")

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun isValid(): Boolean {
        if (etName.text?.trim()?.isEmpty().orFalse()) {
            toastNow("Please enter name")
            return false
        }

        if (etEmail.text?.trim()?.isEmpty().orFalse()) {
            toastNow("Please enter email")
            return false
        }

        if (!etEmail.text?.trim()?.toString()?.isEmail().orFalse()) {
            toastNow("Please enter valid email")
            return false
        }

//        if (etAge.text?.trim()?.isEmpty().orFalse()) {
//            toastNow("Please enter age")
//            return false
//        }

        if (etContactNumber.text?.trim()?.isNotEmpty().orFalse()) {
            if (etContactNumber.text?.trim()?.length.orZero() < 10) {
                toastNow("Please enter valid contact number")
                return false
            }
        }
        return true
    }
}
