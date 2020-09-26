package com.example.dyno.MyPage.Fragment.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.example.dyno.R
import com.example.dyno.VO.CombineVO
import kotlin.math.log

class DFAdapter(context: Context?, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val mContext = context
    private val mObjects = objects
    private val mResource = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var viewHolder: ViewHolder
        var view = convertView
        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(mResource, parent, false)
            viewHolder.textView = view!!.findViewById(R.id.item_dur_name) as TextView
            view.tag = viewHolder
            Log.d("객체확인","dd"+getItem(position)!!.c1)

            viewHolder.text1View.text = getItem(position)!!.c1
            viewHolder.text2View.text = getItem(position)!!.c2

            when (getItem(position)!!.result) {
                -1 -> {
                viewHolder.background.setBackgroundColor(getColor(mContext!!,R.color.dynoCombineRed))
            }
                0 -> {
                    viewHolder.background.setBackgroundColor(getColor(mContext!!,R.color.dynoCombineOrage))
                }
                1 -> {
                    viewHolder.background.setBackgroundColor(getColor(mContext!!,R.color.dynoCombineGreen))
                }
            }

            return view

        } else {
            viewHolder = view.tag as ViewHolder
        }

        return view
    }

    override fun getItem(position: Int): CombineVO? {
        return mObjects[position]
    }

    override fun getCount(): Int {
        return mObjects.size
    }

    inner class ViewHolder{
        lateinit var text1View : TextView
        lateinit var text2View : TextView
        lateinit var background : LinearLayout
    }


}