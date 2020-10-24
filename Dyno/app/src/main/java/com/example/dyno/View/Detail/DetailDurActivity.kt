package com.example.dyno.View.MyPage.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.View.MyPage.Detail.Adapters.DetailDAdapter
import com.example.dyno.R
import com.example.dyno.VO.DurVO
import kotlinx.android.synthetic.main.activity_detail_dur.*

class DetailDurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dur)

        val data = intent.getParcelableExtra<DurVO>("DATA3")
        detail_d_name1.text=data.itemName1

        detail_d_name2.text=data.itemName2

        var rea:String=""
        for(reason in data.durReason){
            rea+=reason
        }
        detail_d_info.text=rea
        //recycler_detail_d1.adapter = DetailDAdapter(this,data.warnMedicineNames1)
        //recycler_detail_d2.adapter = DetailDAdapter(this,data.warnMedicineNames2)
        recycler_detail_d1.layoutManager = LinearLayoutManager(this)         // 이거 해줘야 레이아웃 보임.
        recycler_detail_d2.layoutManager = LinearLayoutManager(this)         // 이거 해줘야 레이아웃 보임.
    }
}