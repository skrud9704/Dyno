package com.example.dyno.Splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.dyno.LocalDB.User
import com.example.dyno.LocalDB.UserDB
import com.example.dyno.MainActivity
import com.example.dyno.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private var userDB:UserDB?=null
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val newUser = User()

        val et = findViewById<EditText>(R.id.userN) //사용자 이름 받아오기 위한 거
        val tv = findViewById<TextView>(R.id.userD)// 사용자 단말기 아이디 보여주는거

        val userDevice = Settings.Secure.getString(this.contentResolver,Settings.Secure.ANDROID_ID)

        tv.text = userDevice
        newUser.dId=userDevice

        Log.d("device info",userDevice)

        button.setOnClickListener{
            userDB=UserDB.getInstance(this)
            newUser.uName = et.text.toString()
            userDB?.userDao()?.insertUser(newUser)

            val pref : SharedPreferences = getSharedPreferences("SaveLogin", Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putString("name",newUser.uName)
            editor.putString("did",newUser.dId)
            editor.apply()

            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }
}