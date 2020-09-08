package com.example.dyno

import android.location.SettingInjectorService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.dyno.LocalDB.User
import com.example.dyno.LocalDB.UserDB
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private var userDB:UserDB?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val newUser = User()

        var et= findViewById<EditText>(R.id.editTextTextPersonName) //사용자 이름 받아오기 위한 거
        var tv= findViewById<TextView>(R.id.userD)// 사용자 단말기 아이디 보여주는거

        var userDevice=Settings.Secure.getString(this.contentResolver,Settings.Secure.ANDROID_ID)
        tv.setText(userDevice)
        newUser.dId=userDevice


        Log.d("device info",userDevice)
        button.setOnClickListener{
            userDB=UserDB.getInstance(this)
            newUser.uName =et.text.toString()
            userDB?.userDao()?.insertUser(newUser)
           

        }


    }
}