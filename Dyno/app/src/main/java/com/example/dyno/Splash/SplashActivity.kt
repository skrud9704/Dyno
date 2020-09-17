package com.example.dyno.Splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.dyno.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(600)
        // 앱에 디바이스 아이디와 회원 명이 이미 저장돼있다면
        // -> 메인 액티비티
        // 그렇지 않다면
        // -> SignUp 액티비티

        val pref : SharedPreferences = getSharedPreferences("SaveLogin", Context.MODE_PRIVATE)
        val prefName = pref.getString("name","null")
        val prefDid = pref.getString("did","null")

        if(prefDid=="null" && prefName=="null"){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}