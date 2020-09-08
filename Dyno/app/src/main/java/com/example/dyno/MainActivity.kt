package com.example.dyno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter=MainAdapter(whatIEatList)
        mPager.adapter=adapter

        val detailInfo= findViewById<Button>(R.id.viewAll)
        detailInfo.setOnClickListener{
            //val intent =Intent(this,)
        }
        val registerDiseasePage=findViewById<Button>(R.id.registerM)
        registerDiseasePage.setOnClickListener{
            val nextIntent=Intent(this,Camera::class.java)
            startActivity(nextIntent)
        }

    }
    companion object{
        val whatIEatList =arrayListOf(
            whatIEat("질병a","의약품 a, 의약품 b,의약품 c"),
            whatIEat("건강기능식품a","기능성원재료")
        )
    }

}
