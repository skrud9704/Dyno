package com.example.dyno

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.layout.view.*

class MainAdapter(private val list:ArrayList<whatIEat>):PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater=LayoutInflater.from(container.context)
        val view=inflater.inflate(R.layout.layout,container,false)

        view.name.text=list[position].name
        view.detail.text=list[position].detail
        view.detail2.text=list[position].detail2
        //제목의 경우 약은 처방전 이름으로
        //건강기능식품은 건강기능식품 이름으로
        //세부정보로 약의 경우는 약 종류를 건강기능식품의 경우는?

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view==obj
    }

    override fun getCount(): Int {
        return list.size

    }


}