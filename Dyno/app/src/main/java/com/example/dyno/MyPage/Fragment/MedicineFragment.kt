package com.example.dyno.MyPage.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.dyno.R
import kotlinx.android.synthetic.main.fragment_medicine.*


class MedicineFragment : Fragment(){

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_medicine,null)

            val medicines : Array<String> = arrayOf("간질환","고혈압","감기","치질")
            val listView = view.findViewById<ListView>(R.id.list_medicine)
            listView.adapter = MFAdapter(context,R.layout.list_item_medicine,medicines)

        return view
    }
}