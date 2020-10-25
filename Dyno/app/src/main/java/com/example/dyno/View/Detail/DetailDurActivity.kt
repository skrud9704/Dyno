package com.example.dyno.View.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.View.Detail.Adapters.DetailDAdapter
import com.example.dyno.R
import com.example.dyno.VO.DurVO
import kotlinx.android.synthetic.main.activity_detail_dur.*

class DetailDurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dur)

        val data = intent.getParcelableExtra<DurVO>("DATA3")

        dur_item1_name.text=data.itemName1
        dur_item1_name_name.text=data.itemName1

        dur_item2_name.text=data.itemName2
        dur_item2_name_name.text=data.itemName2

        dur_item1_list.adapter = DetailDAdapter(this,data.duritems1)
        dur_item2_list.adapter = DetailDAdapter(this,data.duritems2)
        dur_reason_list.adapter = DetailDAdapter(this,data.durReason)

    }
}