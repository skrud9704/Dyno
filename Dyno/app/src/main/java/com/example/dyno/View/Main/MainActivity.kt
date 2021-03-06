package com.example.dyno.View.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.View.MyPage.MyPageActivity
import com.example.dyno.OCR.CameraActivity
import com.example.dyno.R
import com.example.dyno.View.RegistSupplement.RegistSupplementActivity
import com.example.dyno.VO.NowEatVO
import com.example.dyno.View.DashBoard1.DashBoard1Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val userEatList : ArrayList<NowEatVO> = arrayListOf()
    private val adapter : MainAdapter = MainAdapter(this,userEatList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 대시보드 셋팅
        setDashBoard()
        // 페이저 셋팅 (사용자가 현재 복용 중인 약/건강기능식품)
        setPager()
        // 페이저 데이터인 NowEatVO 가져오기
        getData()
        // 각 버튼 클릭 리스너 셋팅
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        getData()
        setDashBoard()
    }

    override fun onRestart() {
        super.onRestart()
        getData()
        setDashBoard()
    }


    private fun setDashBoard(){
        val localDB = RoomDB.getInstance(this)
        not_recommend_count.text = localDB.notRecommmendDAO().getDurItemCount().toString()
        RoomDB.destroyInstance()
    }

    private fun setPager(){
        val dpValue = 40
        val d = resources.displayMetrics.density

        val margin = (dpValue * d).toInt()

        mPager.clipToPadding=false
        mPager.setPadding(margin*14/15,0,margin*14/15,0)
        mPager.pageMargin=margin/3

        mPager.adapter=adapter
    }

    private fun setClickListener(){

        // My Dash Board 1
        dash_board.setOnClickListener{
            val nextIntent=Intent(this, DashBoard1Activity::class.java)
            startActivity(nextIntent)
        }

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
        //Toast.makeText(this,"다시 획득",Toast.LENGTH_SHORT).show()
        val localDB = RoomDB.getInstance(this)
        val diseaseList = localDB.diseaseDAO().getDiseaseMinimal()
        val supplementList = localDB.supplementDAO().getSupplementMinimal()

        userEatList.clear()
        adapter.notifyDataSetChanged()

        for(disease in diseaseList){
            val date = disease.d_date.substring(0,10)
            userEatList.add(NowEatVO(1,disease.d_date,disease.d_name,date,disease.getMedicineNames()))
        }

        for(supplement in supplementList){
            val date = supplement.s_date.substring(0,10)
            userEatList.add(NowEatVO(2,supplement.s_name,supplement.s_name,date,supplement.getIngredients()))
        }

        adapter.notifyDataSetChanged()
    }


}
