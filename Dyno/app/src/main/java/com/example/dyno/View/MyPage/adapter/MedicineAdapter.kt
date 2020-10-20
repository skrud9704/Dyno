package com.example.dyno.View.MyPage.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.View.MyPage.Detail.DetailMedicineActivity
import com.example.dyno.R
import com.example.dyno.VO.DiseaseMinimal
import com.example.dyno.VO.DiseaseVO
import com.example.dyno.VO.MedicineVO
import com.example.dyno.View.MyPage.Detail.DetailSupplementActivity
import kotlinx.android.synthetic.main.recyclerlist_item_mypage.view.*

class MedicineAdapter(private val context: Context) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    // 로컬 DB, DAO 생성.
    private val localDB = RoomDB.getInstance(context)
    private val diseaseDAO = localDB.diseaseDAO()
    // DiseaseMinimal = 현재 페이지에서 필요한 기본적인 정보만 가진 객체
    private val diseaseData : List<DiseaseMinimal> = diseaseDAO.getDiseaseMinimal()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlist_item_mypage, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = diseaseData[position].d_name
        holder.subTitle.text = diseaseData[position].d_date
    }

    override fun getItemCount(): Int {
        return diseaseData.size
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView = itemView.listitem_name
        var subTitle: TextView = itemView.listitem_subname

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // 약 상세정보 화면으로 이동. 현재 DiseaseVO를 넘긴다.
            val selectedData = diseaseDAO.getDisease(diseaseData[adapterPosition].d_date)
            val intent = Intent(context,DetailMedicineActivity::class.java)
            intent.putExtra("DATA_DISEASE",selectedData)
            context.startActivity(intent)
        }
    }


}