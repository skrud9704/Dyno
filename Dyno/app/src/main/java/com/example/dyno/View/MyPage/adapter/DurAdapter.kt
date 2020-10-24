package com.example.dyno.View.MyPage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.View.MyPage.Detail.DetailDurActivity
import com.example.dyno.R
import com.example.dyno.VO.DurVO
import java.util.ArrayList

class DurAdapter : RecyclerView.Adapter<DurAdapter.VersionViewHolder> {
    var versionModels: List<String>? = null
    var isHomeList: Boolean
    var context: Context? = null

    fun setDiseaseList(context: Context) {
        val listArray =
            context.resources.getStringArray(R.array.dur_name)
        val subTitleArray =
            context.resources.getStringArray(R.array.disease_date)
        val colorArray = context.resources.getStringArray(R.array.dur_result)
        homeActivitiesList!!.clear()
        homeActivitiesSubList.clear()
        homeColorList.clear()
        for (i in listArray.indices) {
            homeActivitiesList!!.add(listArray[i])
            homeActivitiesSubList.add(subTitleArray[i])
            homeColorList.add(colorArray[i])
        }
    }

    // context만 넘기면 자체 데모데이터로 넣고
    constructor(context: Context) {
        isHomeList = true
        this.context = context
        setDiseaseList(context)

    }
    // List를 넘기면 실제데이터로 보여줌.
    constructor(versionModels: List<String>?) {
        isHomeList = false
        this.versionModels = versionModels
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VersionViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerlist_item_mypage, viewGroup, false)
        return VersionViewHolder(view)
    }

    override fun onBindViewHolder(versionViewHolder: VersionViewHolder, i: Int) {
        if (isHomeList) {

            versionViewHolder.title.text = homeActivitiesList!![i]
            versionViewHolder.subTitle.text = homeActivitiesSubList[i]
            //1 : 정상, 2 : 조심, 3 : 경고
            if(homeColorList[i]=="1"){
                versionViewHolder.cardItemLayout.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.green_card))
            }else if(homeColorList[i]=="2"){
                versionViewHolder.cardItemLayout.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.orange_card))
            }else if(homeColorList[i]=="3"){
                versionViewHolder.cardItemLayout.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.red_card))
            }

        } else {
            versionViewHolder.title.text = versionModels!![i]
        }
    }

    override fun getItemCount(): Int {
        return if (isHomeList)
            if (homeActivitiesList == null)
                0
            else
                homeActivitiesList!!.size
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

    inner class VersionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cardItemLayout: CardView
        var title: TextView
        var subTitle: TextView


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

        override fun onClick(v: View?) {
            val intent = Intent(context,DetailDurActivity::class.java)
            val testVo : ArrayList<String> = arrayListOf("뮤코세라정","볼그레액")
            val testV2 : ArrayList<String> = arrayListOf("세포독심정")
            /*val testVo3 = DurVO("2016","2016","각막염","2016","감기","","무엇무엇을 유발해서 좋지 않음",testVo,testV2,1)
            intent.putExtra("DATA3",testVo3)
            context!!.startActivity(intent)*/

        }
    }


    companion object {
        var homeActivitiesList: MutableList<String>? =
            ArrayList()
        var homeActivitiesSubList: MutableList<String> =
            ArrayList()
        var homeColorList: MutableList<String> =
            ArrayList()
    }
}