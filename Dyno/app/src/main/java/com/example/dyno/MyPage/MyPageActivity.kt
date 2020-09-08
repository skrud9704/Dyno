package com.example.dyno.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dyno.MyPage.Fragment.MedicineFragment
import com.example.dyno.R
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val myPageAdapter = MyPageAdapter(supportFragmentManager,1)
        viewPager.adapter = myPageAdapter
        tab.setupWithViewPager(viewPager)

    }
}