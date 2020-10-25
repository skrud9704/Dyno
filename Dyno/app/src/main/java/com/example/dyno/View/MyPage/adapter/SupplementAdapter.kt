package com.example.dyno.View.MyPage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.View.Detail.DetailSupplementActivity
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.SupplementMinimal

class SupplementAdapter(private val context: Context) :
    RecyclerView.Adapter<SupplementAdapter.ViewHolder>() {

    // 로컬 DB, DAO 생성.
    private val localDB = RoomDB.getInstance(context)
    private val supplementDAO = localDB.supplementDAO()
    // SupplementMinimal = 현재 페이지에서 필요한 기본적인 정보만 가진 객체
    private val supplementData : List<SupplementMinimal> = supplementDAO.getSupplementMinimal()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlist_item_mypage, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.text = supplementData[i].s_name
        viewHolder.subTitle.text = supplementData[i].s_date
    }

    override fun getItemCount(): Int {
        return supplementData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var title: TextView = itemView.findViewById(R.id.listitem_name)
        var subTitle: TextView = itemView.findViewById(R.id.listitem_subname)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // 건강기능식품 상세정보 화면으로 이동. 현재 SupplementVO 넘긴다.
            val selectedData = supplementDAO.getSupplement(supplementData[adapterPosition].s_name)
            val intent = Intent(context, DetailSupplementActivity::class.java)
            intent.putExtra("DATA2",selectedData)
            context.startActivity(intent)

        }


    }

}