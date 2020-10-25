package com.example.dyno.View.MyPage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.View.MyPage.Detail.DetailDurActivity
import com.example.dyno.R
import com.example.dyno.VO.DurVO
import kotlinx.android.synthetic.main.recyclerlist_item_mypage.view.*
import java.util.ArrayList

class DurAdapter(private val context: Context) : RecyclerView.Adapter<DurAdapter.ViewHolder>() {

    // 로컬 DB, DAO 생성.
    private val localDB = RoomDB.getInstance(context)
    private val durDAO = localDB.durDAO()
    private val durList : List<DurVO> = durDAO.getDurList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlist_item_mypage, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if(durList[i].type==1) {
            viewHolder.title.text = "${durList[i].itemName1}의 처방약품 + ${durList[i].itemName2}의 처방약품"
            viewHolder.cardItemLayout.setCardBackgroundColor(ContextCompat.getColor(context,R.color.purple_50))
        }
        else if(durList[i].type==2){
            viewHolder.title.text = "${durList[i].itemName1}의 처방약품 + ${durList[i].itemName2}"
            viewHolder.cardItemLayout.setCardBackgroundColor(ContextCompat.getColor(context,R.color.cyan_50))
        }


        viewHolder.subTitle.text = durList[i].date
    }

    override fun getItemCount(): Int {
        return durList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cardItemLayout: CardView = itemView.cardlist_item
        var title: TextView = itemView.listitem_name
        var subTitle: TextView = itemView.listitem_subname

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(context,DetailDurActivity::class.java)
            val testVo : ArrayList<String> = arrayListOf("뮤코세라정","볼그레액")
            val testV2 : ArrayList<String> = arrayListOf("세포독심정")

            var re:ArrayList<String> = arrayListOf("무엇무엇을 유발해서 좋지 않음")
            val testVo3 = DurVO("2016",1,"2016","각막염","2016","감기",testVo,testV2,re)

            intent.putExtra("DATA3",testVo3)
            context.startActivity(intent)

        }
    }

}