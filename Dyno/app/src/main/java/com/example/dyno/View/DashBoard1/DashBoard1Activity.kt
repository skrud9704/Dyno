package com.example.dyno.View.DashBoard1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import kotlinx.android.synthetic.main.activity_dash_board1.*
import kotlinx.android.synthetic.main.recyclerlist_item_dash_board1.*

class DashBoard1Activity : AppCompatActivity() {
    private lateinit var adapt:DashBoardAdapter
    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board1)

        val locaDB = RoomDB.getInstance(this)
        Log.d(TAG,locaDB.notRecommmendDAO().getAllNotRecommend().size.toString())
        adapt = DashBoardAdapter(this,locaDB.notRecommmendDAO().getAllNotRecommend())

        dash_boardR.adapter=adapt
    }



}