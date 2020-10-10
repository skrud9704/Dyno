package com.example.dyno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.MyPage.MyPageActivity
import com.example.dyno.OCR.CameraActivity
import com.example.dyno.RegistSupplement.RegistSupplementActivity
import com.example.dyno.VO.NowEatVO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 페이저 데이터인 NowEatVO 가져오기
        getData()
        // 페이저 셋팅 (사용자가 현재 복용 중인 약/건강기능식품)
        setPager()
        // 각 버튼 클릭 리스너 셋팅
        setClickListener()
    }

    private fun setPager(){
        val dpValue = 40
        val d = resources.displayMetrics.density

        val margin = (dpValue * d).toInt()

        mPager.clipToPadding=false
        mPager.setPadding(margin*11/12,0,margin*11/12,0)
        mPager.pageMargin=margin/2

        val adapter=MainAdapter(this,whatIEatList)
        mPager.adapter=adapter
    }

    private fun setClickListener(){
        // 의약품 등록
        registerM.setOnClickListener{
            val nextIntent=Intent(this, CameraActivity::class.java)
            nextIntent.putExtra("DATA","medicine")
            startActivity(nextIntent)
        }

        // 건강기능식품 등록
        registerS.setOnClickListener {
            val nextIntent = Intent(this,RegistSupplementActivity::class.java)
            startActivity(nextIntent)
        }

        // 병용판단 - 없앰.
        /*dur.setOnClickListener{
            val intent=Intent(this, DurActivity::class.java)
            startActivity(intent)
        }*/

        // 마이페이지
        myPage.setOnClickListener{
            val myPageIntent=Intent(this, MyPageActivity::class.java)
            startActivity(myPageIntent)
        }
    }

    private fun getData(){

    }


    companion object{
        val whatIEatList =arrayListOf(
            NowEatVO(
                "급성 아토피결막염",
                "하메론에이점안액\n톨론점안액\n올로텐플러스점안액",
                "60mg/1회\n1방울/1회\n1방울/1회", "2020-09-26", 0
            ),
            NowEatVO(
                "락토바이옴 장용성",
                "산화아연",
                "",
                "2020-09-26",
                1
            )
        )
    }

}
