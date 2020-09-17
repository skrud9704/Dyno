package com.example.dyno.MyPage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dyno.MyPage.Fragment.MedicineFragment
import com.example.dyno.R
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_NAME = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val myPageAdapter = MyPageAdapter(supportFragmentManager,1)
        viewPager.adapter = myPageAdapter
        tab.setupWithViewPager(viewPager)

        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefName = pref.getString(SHARED_PREF_NAME,"null")
        if(prefName!="null")
            myPageName.text = prefName

    }
}