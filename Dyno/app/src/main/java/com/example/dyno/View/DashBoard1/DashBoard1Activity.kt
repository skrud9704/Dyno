package com.example.dyno.View.DashBoard1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import com.example.dyno.View.DashBoard1.Adapters.DashBoardAdapter
import kotlinx.android.synthetic.main.activity_dash_board1.*

class DashBoard1Activity : AppCompatActivity() {
    private lateinit var adapt: DashBoardAdapter
    private val TAG = this::class.java.simpleName
    private lateinit var localDB : RoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board1)

        getData()

    }

    private fun getData(){
        localDB = RoomDB.getInstance(this)
        Log.d(TAG,localDB.notRecommmendDAO().getAllNotRecommend().size.toString())
        adapt = DashBoardAdapter(
            this,
            localDB.notRecommmendDAO().getAllNotRecommend()
        )

        dash_boardR.adapter=adapt
    }

    override fun onResume() {
        super.onResume()
        adapt.setNewData(localDB.notRecommmendDAO().getAllNotRecommend())
    }



}