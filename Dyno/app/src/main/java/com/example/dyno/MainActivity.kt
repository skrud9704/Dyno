package com.example.dyno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.dyno.MyPage.MyPageActivity
import com.example.dyno.RegistMedicine.Camera
import com.example.dyno.RegistSupplement.RegistSupplementActivity
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
        val combineUse=findViewById<Button>(R.id.combineUse)
        combineUse.setOnClickListener{
            val intent=Intent(this, ServerExample::class.java)
            startActivity(intent)
        }

    }
    companion object{
        val whatIEatList =arrayListOf(
            whatIEat("질병a","의약품 a, 의약품 b,의약품 c"),
            whatIEat("건강기능식품a","기능성원재료")
        )
    }

}
