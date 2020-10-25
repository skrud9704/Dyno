package com.example.dyno.View.Detail.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import kotlinx.android.synthetic.main.recyclerlist_item_expandable_dur.view.*

class DetailDAdapter(private val context: Context, private val data : ArrayList<String>)
    : RecyclerView.Adapter<DetailDAdapter.ViewHolder>() {

    // private val data 는 1) 병용불가 판단된 의약품들, 2) 병용불가 판단된 건강기능식품기능성원재료, 3) 병용불가이유
    // 세 종류의 ArrayList일 수 있다.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_expandable_dur, parent, false)
        return ViewHolder(view)
    }

    // 카운트를 리턴하지 않으면 onCreateViewHolder 를 부르지 않음. 즉 리턴된 카운트 수만큼 onCreateViewHolder 를 부름.
    override fun getItemCount(): Int {
        return data.size
    }

    // Data 와 커스텀 ViewHolder를 Bind하는 부분.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.elemNameTv.text = data[position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var elemNameTv : TextView = itemView.dur_elem_name

    }

}