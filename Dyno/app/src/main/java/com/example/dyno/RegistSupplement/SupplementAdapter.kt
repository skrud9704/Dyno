package com.example.dyno.RegistSupplement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.list_item_search_supplement.view.*

class SupplementAdapter(val supplements : ArrayList<SupplementVO>, val context: Context)
    : RecyclerView.Adapter<SupplementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupplementAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_supplement,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return supplements.size
    }

    override fun onBindViewHolder(holder: SupplementAdapter.ViewHolder, position: Int) {
        holder.itemView.item_no?.text = supplements.get(position).m_no.toString()
        holder.itemView.item_name?.text = supplements.get(position).m_name
        holder.itemView.item_company?.text = supplements.get(position).m_company
        holder.itemView.item_num?.text = supplements.get(position).m_num.toString()
        holder.itemView.item_date?.text = supplements.get(position).m_date

        holder.itemView.setOnClickListener {
            Toast.makeText(context,"Clicked: ${supplements.get(position).m_name}",Toast.LENGTH_SHORT).show()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


}