package com.example.dyno.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.R
import com.example.dyno.VO.DiseaseVO
import kotlinx.android.synthetic.main.activity_detail_medicine.*

class DetailMedicineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_medicine)

        val data = intent.getParcelableExtra<DiseaseVO>("DATA")

        test_m.text = data.medicines[0].mCode

    }
}