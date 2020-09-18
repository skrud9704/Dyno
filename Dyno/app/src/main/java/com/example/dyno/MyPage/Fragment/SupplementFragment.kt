package com.example.dyno.MyPage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.dyno.MyPage.Fragment.Adapters.MFAdapter
import com.example.dyno.MyPage.Fragment.Adapters.SFAdapter
import com.example.dyno.R

class SupplementFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_supplement,null)


        val supplements : Array<String> = arrayOf(
            "센트룸 아쿠아비타 발포 멀티비타민 딸기향 30포",
            "센트룸 포 우먼 멀티비타민 미네랄(50정)",
            "California Gold Nutrition, LactoBif 프로바이오틱스, 300억 CFU, 베지 캡슐 60정",
            "Now Foods, 비타민 D-3, 고효능, 5,000 IU, 120 소프트젤",
            "Sports Research, 아스타잔틴 함유 남극 크릴 오일, 1000mg, 소프트젤 60정",
            "Sports Research, 오메가-3 피쉬 오일, Triple Strength, 1,250mg, 소프트젤 180정",
            "광동우황청심원(천연사향함유) 액제/환제")

        val listView = view.findViewById<ListView>(R.id.list_supplement)
        listView.adapter =
            SFAdapter(
                context,
                R.layout.list_item_mypage_supplement,
                supplements
            )

        return view
    }
}