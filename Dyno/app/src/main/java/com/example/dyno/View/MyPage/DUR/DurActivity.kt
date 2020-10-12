package com.example.dyno.DUR

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.DUR.Adapters.DMAdapter
import com.example.dyno.DUR.Adapters.DSAdapter
import com.example.dyno.R
import kotlinx.android.synthetic.main.activity_dur.*

class DurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dur)
        val medicines : Array<String> = arrayOf("간질환","고혈압","감기","치질","심장병","고지혈증","우울증","각막염","중이염","가벼운 화상")
        val supplements : Array<String> = arrayOf("센트룸 아쿠아비타 발포 멀티비타민 딸기향 30포",
            "센트룸 포 우먼 멀티비타민 미네랄(50정)",
            "California Gold Nutrition, LactoBif 프로바이오틱스, 300억 CFU, 베지 캡슐 60정",
            "Now Foods, 비타민 D-3, 고효능, 5,000 IU, 120 소프트젤",
            "Sports Research, 아스타잔틴 함유 남극 크릴 오일, 1000mg, 소프트젤 60정",
            "Sports Research, 오메가-3 피쉬 오일, Triple Strength, 1,250mg, 소프트젤 180정",
            "광동우황청심원(천연사향함유) 액제/환제")
        list_dur_m.adapter = DMAdapter(this,R.layout.list_item_dur_medicine,medicines)
        list_dur_s.adapter = DSAdapter(this,R.layout.list_item_dur_supplement,supplements)
    }
}