package com.example.dyno.View.RegistMedicine

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.DiseaseGuessVO
import kotlinx.android.synthetic.main.recyclerlist_item_reg_disease.view.*

class DiseaseAdapter(private val context: Context, private var data : DiseaseGuessVO)
: RecyclerView.Adapter<DiseaseAdapter.ViewHolder>(){

    // 체크박스 체크 됐는지 확인하는 용도
    private lateinit var diseaseCheck : BooleanArray

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_reg_disease, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.diseaseNameList.size
    }

    fun setAdapterData(data:DiseaseGuessVO){
        this.data = data
        this.diseaseCheck = BooleanArray(data.diseaseNameList.size){ _ -> false}
        notifyDataSetChanged()
    }

    fun getDiseaseCheck() : BooleanArray{
        return this.diseaseCheck
    }

    fun getDiseaseName(position : Int) : String{
        return data.diseaseNameList[position]
    }

    fun getDiseaseCode(position : Int) : String{
        return data.diseaseCodeList[position]
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 약봉투 -> 질병 추측
        if(data.type==1){
            holder.rank.text = "${position+1}위"
            holder.per.text = data.diseasePerList[position]
        }
        // 처방전 -> 질병 읽어오기
        else if(data.type==2){
            holder.rank.visibility=View.GONE
            holder.per.visibility=View.GONE
            holder.check.visibility=View.GONE
        }
        holder.name.text = data.diseaseNameList[position]
        holder.code.text = data.diseaseCodeList[position]


    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rank: TextView = itemView.disease_rank
        val name : TextView = itemView.disease_name
        val code : TextView = itemView.disease_code
        val per : TextView = itemView.disease_per
        val check : CheckBox = itemView.disease_check

        init{
            check.setOnCheckedChangeListener { _, isChecked ->
                diseaseCheck[adapterPosition] = isChecked
            }
        }

    }
}