package com.example.dyno.MyPage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.Detail.DetailSupplementActivity
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO

class SupplementAdapter : RecyclerView.Adapter<SupplementAdapter.ViewHolder> {
    var versionModels: List<String>? = null

    var isHomeList: Boolean
    var context: Context? = null

    fun setSupplementList(context: Context) {
        val listArray =
            context.resources.getStringArray(R.array.supplement_name)
        val subTitleArray =
            context.resources.getStringArray(R.array.disease_date)
        nameList!!.clear()
        dateList.clear()
        for (i in listArray.indices) {
            nameList!!.add(listArray[i])
            dateList.add(subTitleArray[i])
        }
    }

    constructor(context: Context) {
        isHomeList = true
        this.context = context
        setSupplementList(context)
    }

    constructor(versionModels: List<String>?) {
        isHomeList = false
        this.versionModels = versionModels
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlist_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (isHomeList) {
            viewHolder.title.text = nameList!![i]
            viewHolder.subTitle.text = dateList[i]
        } else {
            viewHolder.title.text = versionModels!![i]
        }
    }

    override fun getItemCount(): Int {
        return if (isHomeList)
            if (nameList == null)
                0
            else
                nameList!!.size
        else if (versionModels == null)
            0
        else
            versionModels!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cardItemLayout: CardView
        var title: TextView
        var subTitle: TextView
        override fun onClick(v: View?) {
            // 건강기능식품 상세정보 화면으로 이동. 현재 SupplementVO 넘긴다.
            val testVo : ArrayList<String> = arrayListOf(
                "곤약감자추출물",
                "비타민C",
                "아연"
            )
            val testVo2 : ArrayList<String> = arrayListOf(
                "피부 보습에 도움을 줄 수 있음",
                "① 결합조직 형성과 기능유지에 필요\n② 철의 흡수에 필요\n③ 항산화 작용을 하여 유해산소로부터 세포를 보호하는데 필요",
                "① 정상적인 면역 기능에 필요\n② 정상적인 세포 분열에 필요"
            )
            //constructor(company: String, name: String, date : String, ingredients : ArrayList<String>, infos : ArrayList<String>) : this() {
            val testVo3 = SupplementVO("세라미드 맥스","(주)한국씨엔에스팜","2020-01-20",testVo,testVo2)
            val intent = Intent(context, DetailSupplementActivity::class.java)
            intent.putExtra("DATA2",testVo3)
            context!!.startActivity(intent)
        }

        init {
            cardItemLayout = itemView.findViewById(R.id.cardlist_item)
            title = itemView.findViewById(R.id.listitem_name)
            subTitle = itemView.findViewById(R.id.listitem_subname)
            if (isHomeList) {
                itemView.setOnClickListener(this)
            } else {
                subTitle.visibility = View.GONE
            }
        }
    }

    companion object {
        var nameList: MutableList<String>? =
            ArrayList()
        var dateList: MutableList<String> =
            ArrayList()
    }
}