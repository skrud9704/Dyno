package com.example.dyno.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_detail_supplement.*

class DetailSupplementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_supplement)

        val data = intent.getParcelableExtra<SupplementVO>("DATA2")

        test_s.text = data.m_ingredients[0]

    }
}