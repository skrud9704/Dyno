package com.example.dyno.DUR.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dyno.R

class DMAdapter (context: Context?, resource: Int, objects: Array<String>) :
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
            viewHolder.textView = view!!.findViewById(R.id.dur_medicine_name) as TextView
            view.tag = viewHolder
            viewHolder.textView.text = getItem(position)

            return view

        } else {
            viewHolder = view.tag as ViewHolder
        }

        return view
    }

    override fun getItem(position: Int): String? {
        return mObjects[position]
    }

    override fun getCount(): Int {
        return mObjects.size
    }

    inner class ViewHolder{
        lateinit var textView : TextView
    }
}