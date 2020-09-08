package com.example.dyno.MyPage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dyno.MyPage.Fragment.CombineFragment
import com.example.dyno.MyPage.Fragment.MedicineFragment
import com.example.dyno.MyPage.Fragment.SupplementFragment

class MyPageAdapter(fm: FragmentManager, behavior: Int): FragmentPagerAdapter(fm, behavior) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {MedicineFragment()}
            1 -> {SupplementFragment()}
            else -> {return CombineFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "의약품"
            1 -> "건강기능식품"
            else -> {return "병용섭취"}
        }
    }

}