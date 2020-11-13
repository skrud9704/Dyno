package com.example.dyno.View.DashBoard1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import com.example.dyno.View.DashBoard1.Adapters.DetailAdapter
import kotlinx.android.synthetic.main.activity_dash_board1_detail.*

class DashBoard1DetailActivity : AppCompatActivity() {
    private lateinit var notRecommentList : List<NotRecommendVO>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board1_detail)

        getData()

        setList()
    }

    private fun getData(){
        if(intent.hasExtra("DATA")){
            val key = intent.getStringExtra("DATA")
            val localDB = RoomDB.getInstance(this)
            notRecommentList = localDB.notRecommmendDAO().getNotRecommend(key)!!

            warn_detail_s_name.text = key

        }
    }


    private fun setList(){
        warn_detail_d_list.adapter = DetailAdapter(this,notRecommentList)
    }
}