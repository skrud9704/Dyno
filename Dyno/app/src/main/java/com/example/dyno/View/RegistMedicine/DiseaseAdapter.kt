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

class DiseaseAdapter(private val context: Context, private var data : ArrayList<String>, private  var data2: ArrayList<String>)
: RecyclerView.Adapter<DiseaseAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_mypage, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = data[position]
        holder.subTitle.text=data2[position]
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.listitem_name
        var subTitle: TextView = itemView.listitem_subname

    }
}