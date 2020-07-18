package com.example.dyno

import android.location.SettingInjectorService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var et=findViewById(R.id.editTextTextPersonName) as EditText
        var userDevice=Settings.Secure.getString(this.contentResolver,Settings.Secure.ANDROID_ID)
        userD.setText(userDevice)

        Log.d("device info",userDevice)
        button.setOnClickListener{
            var userName : String =et.text.toString()

        }


    }
}