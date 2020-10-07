package com.example.dyno.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dyno.Detail.Adapters.DetailSAdapter
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_detail_supplement.*

class DetailSupplementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_supplement)

        val data = intent.getParcelableExtra<SupplementVO>("DATA2")

        detail_s_name.text = data.m_name
        detail_s_date.text = data.m_date

        val detailSAdapter = DetailSAdapter(this,data.m_ingredients,data.m_ingredients_info)
        recycler_detail_s.adapter = detailSAdapter
        recycler_detail_s.layoutManager=LinearLayoutManager(this)

    }
}