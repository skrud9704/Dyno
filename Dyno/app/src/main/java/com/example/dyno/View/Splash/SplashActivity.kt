package com.example.dyno.View.Splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.dyno.View.Main.MainActivity

class SplashActivity : AppCompatActivity() {
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_NAME = "name"
    var SHARED_PREF_DID = "did"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(2000)
        // 앱에 디바이스 아이디와 회원 명이 이미 저장돼있다면
        // -> 메인 액티비티
        // 그렇지 않다면
        // -> SignUp 액티비티

        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefName = pref.getString(SHARED_PREF_NAME,"null")
        val prefDid = pref.getString(SHARED_PREF_DID,"null")

        if(prefDid=="null" && prefName=="null"){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}