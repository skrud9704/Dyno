package com.example.dyno.View.MyPage.Detail.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.MedicineVO
import kotlinx.android.synthetic.main.recyclerlist_item_expandable_medicine.view.*

class DetailMAdapter(context: Context, medicines : ArrayList<MedicineVO>)
    : RecyclerView.Adapter<DetailMAdapter.ViewHolder>() {

    // 한 질병 아래 처방된 의약품 정보
    var medicineModels: ArrayList<MedicineVO>
    // context
    var context: Context

    // 초기화. (init을 지우고 위에 맴버변수에 바로 할당해도 됨.)
    init {
        this.medicineModels = medicines
        this.context = context
    }

    // 아래에 inner class로 커스텀 ViewHolder 만듬. -> implement 함.
    // 리사이클러뷰 아이템 하나에 대한 layout을 inflate해서 커스텀ViewHolder를 생성해 리턴하는 것.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_expandable_medicine, parent, false)
        return ViewHolder(view)
    }

    // 카운트를 리턴하지 않으면 onCreateViewHolder를 부르지 않음. 즉 리턴된 카운트 수만큼 onCreateViewHolder를 부름.
    override fun getItemCount(): Int {
        return medicineModels.size
    }

    // Data 와 커스텀 ViewHolder를 Bind하는 부분.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 의약품이름 TextView
        holder.medicine_tv.text = medicineModels[position].name

        // 의약품 복용법 TextView   ex) 1정씩/3회/3일  amount/count/total
        /*val amount = medicineModels[position].amount.toString()
        val count = medicineModels[position].count.toString()
        val total = medicineModels[position].total.toString()
        val sb = StringBuilder()
        holder.dosage_tv.text = sb.append(amount).append("씩/")
                                  .append(count).append("회/")
                                  .append(total).append("일").toString()*/
        // 의약품상세 TextView
        holder.info_tv.text = medicineModels[position].dosage

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var medicine_tv : TextView
        var dosage_tv : TextView
        var info_tv : TextView
        var info_layout : LinearLayout

        init{
            medicine_tv = itemView.medicine_name
            dosage_tv = itemView.medicine_dosage
            info_tv = itemView.medicine_info
            info_layout = itemView.m_info_layout
        }

    }

}