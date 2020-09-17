package com.example.dyno.MyPage.Fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dyno.R

class MFAdapter(context: Context?, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val mContext = context
    private val mObjects = objects
    private val mResource = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var viewHolder: ViewHolder
        var view = convertView

        if(view==null){
            viewHolder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(mResource,parent,false)
            viewHolder.textView = view.findViewById(R.id.item_medicine_name)
            view.tag = viewHolder
            viewHolder.textView.text = mObjects[position]

            return view

        }else{
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.textView.text = mObjects[position]
        return view

    }

    inner class ViewHolder{
        lateinit var textView : TextView
    }
}