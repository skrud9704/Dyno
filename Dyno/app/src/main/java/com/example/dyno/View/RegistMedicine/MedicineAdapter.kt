package com.example.dyno.View.RegistMedicine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.MedicineVO
import kotlinx.android.synthetic.main.recyclerlist_item_mypage.view.*

class MedicineAdapter (private val context: Context, private var data : ArrayList<MedicineVO>)
    : RecyclerView.Adapter<MedicineAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_mypage, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = data[position].name
        holder.subTitle.text = data[position].dosage
    }

    fun getNewData(data : ArrayList<MedicineVO>){
        this.data.clear()
        this.data = data
        notifyDataSetChanged()
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.listitem_name
        var subTitle: TextView = itemView.listitem_subname
    }
}