package com.example.dyno.View.DashBoard1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import kotlinx.android.synthetic.main.activity_dash_board1.*
import kotlinx.android.synthetic.main.recyclerlist_item_dash_board1.*

class DashBoard1Activity : AppCompatActivity() {
    private val adapt:DashBoardAdapter = DashBoardAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board1)
        dash_boardR.adapter=adapt
    }



}