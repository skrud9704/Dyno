package com.example.dyno.MyPage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.dyno.MyPage.Fragment.Adapters.CFAdapter
import com.example.dyno.MyPage.Fragment.Adapters.MFAdapter
import com.example.dyno.R
import com.example.dyno.VO.CombineVO

class CombineFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_combine,null)

        val combines : ArrayList<CombineVO> = arrayListOf(
            CombineVO("약1","건1",0),
            CombineVO("약1","건2",0),
            CombineVO("약1","건3",0),
            CombineVO("약2","건1",1),
            CombineVO("약2","건2",1),
            CombineVO("약2","건3",1),
            CombineVO("약1","건1",-1),
            CombineVO("약3","건1",-1)
        )
        val listView = view.findViewById<ListView>(R.id.list_combine)
        listView.adapter =
            CFAdapter(
                context,
                R.layout.list_item_mypage_combine,
                combines
            )

        return view
    }
}