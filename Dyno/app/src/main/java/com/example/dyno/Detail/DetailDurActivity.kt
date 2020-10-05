package com.example.dyno.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.R
import com.example.dyno.VO.CombineVO
import kotlinx.android.synthetic.main.activity_detail_dur.*

class DetailDurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dur)

        val data = intent.getParcelableExtra<CombineVO>("DATA")

        test_d.text = data.c1
    }
}