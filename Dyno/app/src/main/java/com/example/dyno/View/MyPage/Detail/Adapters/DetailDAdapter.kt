package com.example.dyno.Detail.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.MedicineVO
import kotlinx.android.synthetic.main.recyclerlist_item_expandable_dur.view.*
import kotlinx.android.synthetic.main.recyclerlist_item_expandable_medicine.view.*

class DetailDAdapter(context: Context, warnList1 : ArrayList<String>)
    : RecyclerView.Adapter<DetailDAdapter.ViewHolder>() {

    // 병용판단한 인자(질병/건강기능식품)에서 위험한
    // 의약품(질병)/주성분(건강기능식품) 리스트
    var warnList1: ArrayList<String>
    // context
    var context: Context

    // 초기화. (init을 지우고 위에 맴버변수에 바로 할당해도 됨.)
    init {
        this.warnList1 = warnList1
        this.context = context
    }

    // 아래에 inner class로 커스텀 ViewHolder 만듬. -> implement 함.
    // 리사이클러뷰 아이템 하나에 대한 layout을 inflate해서 커스텀ViewHolder를 생성해 리턴하는 것.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_expandable_dur, parent, false)
        return ViewHolder(view)
    }

    // 카운트를 리턴하지 않으면 onCreateViewHolder를 부르지 않음. 즉 리턴된 카운트 수만큼 onCreateViewHolder를 부름.
    override fun getItemCount(): Int {
        return warnList1.size
    }

    // Data 와 커스텀 ViewHolder를 Bind하는 부분.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.elem_name_tv.text = warnList1[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var elem_name_tv : TextView = itemView.dur_elem_name

    }

}