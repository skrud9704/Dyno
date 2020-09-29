package com.example.dyno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.dyno.DUR.DurActivity
import com.example.dyno.MyPage.MyPageActivity
import com.example.dyno.RegistMedicine.Camera
import com.example.dyno.RegistSupplement.RegistSupplementActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dpValue = 40
        val d = resources.displayMetrics.density

        val margin = (dpValue * d).toInt()

        mPager.clipToPadding=false
        mPager.setPadding(margin*11/12,0,margin*11/12,0)
        mPager.pageMargin=margin/2

        val adapter=MainAdapter(this,whatIEatList)
        mPager.adapter=adapter

        val registerDiseasePage=findViewById<Button>(R.id.registerM)
        registerDiseasePage.setOnClickListener{
            val nextIntent=Intent(this, Camera::class.java)
            startActivity(nextIntent)
        }

        val registerSupplementPage=findViewById<Button>(R.id.registerS)
        registerSupplementPage.setOnClickListener {
            val nextIntent = Intent(this,RegistSupplementActivity::class.java)
            startActivity(nextIntent)
        }

        val myPage=findViewById<TextView>(R.id.myPage)
        myPage.setOnClickListener{
            val myPageIntent=Intent(this,
                MyPageActivity::class.java)
            startActivity(myPageIntent)
        }
        val dur=findViewById<Button>(R.id.dur)
        dur.setOnClickListener{
            val intent=Intent(this, DurActivity::class.java)
            startActivity(intent)
        }

    }
    companion object{
        val whatIEatList =arrayListOf(
            whatIEat("급성 아토피결막염",
                "하메론에이점안액\n톨론점안액\n올로텐플러스점안액",
                "60mg/1회\n1방울/1회\n1방울/1회","2020-09-26",0),

            whatIEat("가려움증",
                "아레그라정",
                "1정/2회","2020-09-26",0),

            whatIEat("락토바이옴 장용성","산화아연","","2020-09-26",1)
        )

        val demo = whatIEat("고려인삼차", "인삼", "","2020-09-26",1)
    }

}
