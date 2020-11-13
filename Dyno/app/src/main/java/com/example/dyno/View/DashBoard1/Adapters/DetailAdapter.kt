package com.example.dyno.View.DashBoard1.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import com.example.dyno.View.DashBoard1.DashBoard1DetailActivity
import kotlinx.android.synthetic.main.recyclerlist_item_dash_board1_detail.view.*

class DetailAdapter (private val context: Context, private var data : List<NotRecommendVO>)
    : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private lateinit var listener: DashBoard1DetailActivity.OnItemDeleteClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_dash_board1_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DetailAdapter.ViewHolder, position: Int) {
        holder.d_name.text = data[position].d_name
        holder.d_date.text = data[position].d_id
        holder.warn.text = data[position].reason
    }

    fun setListener(listener : DashBoard1DetailActivity.OnItemDeleteClickListener){
        this.listener = listener
    }

    fun setNewData(data : List<NotRecommendVO>){
        this.data = data
        notifyDataSetChanged()
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val d_image = itemView.warn_disease_icon
        val d_name : TextView = itemView.warn_disease_name
        val d_date : TextView = itemView.warn_disease_date
        val warn : TextView= itemView.warn_reason
        val delete = itemView.warn_disease_delete

        init {
            delete.setOnClickListener {
                listener.onClick(data[adapterPosition].d_id)
            }
        }


    }


}