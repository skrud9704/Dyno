package com.example.dyno.RegistSupplement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.list_item_search_supplement.view.*

class SupplementAdapter(context: Context?, resource: Int, objects: ArrayList<SupplementVO>) :
    ArrayAdapter<SupplementVO>(context, resource, objects) {

    private val mContext = context
    private val mResource = resource
    val data = arrayListOf<SupplementVO>()
    /*val data = arrayListOf(
        SupplementVO("20","고려인삼차","대동고려삼(주)"),
        SupplementVO("19","고려인삼차","대동고려삼(주)"),
        SupplementVO("18", "고려인삼차(수출용)","(주)케이지앤에프"),
        SupplementVO("17","고려인삼차플러스","(주)농협홍삼"),
        SupplementVO("16","고려인삼차","(주)화인내츄럴"),
        SupplementVO("15","학표고려인삼차","(주)케이지앤에프"),
        SupplementVO("14","고려인삼차","(주)농협홍삼"),
        SupplementVO("13","고려인삼차파워","(주)케이지앤에프"),
        SupplementVO("12","진스트 고려인삼차(수출용제품명:진스트 15 고려인삼차GINST 15 KOREAN GINSENG TEA)","(주)일화"),
        SupplementVO("11","고려인삼차","주식회사 유림 고려홍삼"),
        SupplementVO("10","진스트 고려인삼차(GINST KOREAN GINSENG TEA)-전량수출용","(주)일화"),
        SupplementVO("9","고려인삼차골드","농업회사법인 (주)삼흥"),
        SupplementVO("8","고려천일인삼차","(주)화인내츄럴"),
        SupplementVO("7","고려인삼차골드","(주)케이지앤에프"),
        SupplementVO("6","고려인삼차","(주)유니쎌팜"),
        SupplementVO("5","고려인삼차골드","(주)건보"),
        SupplementVO("4","고려인삼차","(주)일화"),
        SupplementVO("3","고려인삼차","고려인삼과학주식회사"),
        SupplementVO("2","고려인삼성분인삼차골드(전량수출용)","고려인삼제조주식회사"),
        SupplementVO("1","고려인삼차성분골드(전량수출용)","고려인삼제조주식회사")
    )*/
    private val mObjects = data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var viewHolder: ViewHolder
        var view = convertView

        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(mResource, parent, false)
            viewHolder.tv_no = view!!.findViewById(R.id.item_no) as TextView
            viewHolder.tv_name = view!!.findViewById(R.id.item_name) as TextView
            viewHolder.tv_company = view!!.findViewById(R.id.item_company) as TextView
            view.tag = viewHolder
            viewHolder.tv_no.text = ""+position+1
            viewHolder.tv_name.text = getItem(position)!!.m_name
            viewHolder.tv_company.text = getItem(position)!!.m_company

            return view

        } else {
            viewHolder = view.tag as ViewHolder
        }

        return view
    }

    override fun getItem(position: Int): SupplementVO? {
        return mObjects[position]
    }

    override fun getCount(): Int {
        return mObjects.size
    }

    inner class ViewHolder{
        lateinit var tv_no : TextView
        lateinit var tv_name : TextView
        lateinit var tv_company : TextView

    }


}