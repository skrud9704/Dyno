package com.example.dyno.RegistSupplement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.recyclerlist_item_reg_supplement.view.*

class SupplementAdapter(val context: Context, var data : ArrayList<SupplementVO>)
    : RecyclerView.Adapter<SupplementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_reg_supplement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.s_no.text = position.toString()
        holder.s_name.text = data[position].m_name
        holder.s_company.text = data[position].m_company
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var s_name = itemView.item_name
        var s_no = itemView.item_no
        var s_company = itemView.item_company

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }




}