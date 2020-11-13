package com.example.dyno.View.DashBoard1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import kotlinx.android.synthetic.main.recyclerlist_item_dash_board1.view.*


class DashBoardAdapter(private val context: Context):
    RecyclerView.Adapter<DashBoardAdapter.ViewHolder>(){
    private val localDB= RoomDB.getInstance(context)
    private val notRecommendDao=localDB.notRecommmendDAO()
    private val list:List<NotRecommendVO> = notRecommendDao.getAllNotRecommend()




    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var s_in=list[position].s_ingredient
        holder.ig_name.text=s_in
        var count:Int=notRecommendDao.getCountforEach(s_in)
        holder.ig_count.text = count.toString()
        holder.ig_dname.text=list[position].d_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View =LayoutInflater.from(parent.context)
           .inflate(R.layout.recyclerlist_item_dash_board1, parent,false)
        return ViewHolder(view)

    }

    inner class ViewHolder (itemView: View):
        RecyclerView.ViewHolder(itemView),View.OnClickListener {

        val ig_name = itemView.warn_ingredient_name
        val ig_count = itemView.warn_ingredient_count
        val ig_dname=itemView.warn_ingredient_name
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val data=list[adapterPosition].s_ingredient
            val intent = Intent(context,DashBoard1DetailActivity::class.java)
            intent.putExtra("DATA",data)
            context.startActivity(intent)
        }

    }

}