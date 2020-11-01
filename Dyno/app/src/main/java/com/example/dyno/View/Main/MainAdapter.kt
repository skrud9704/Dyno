package com.example.dyno.View.Main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.dyno.R
import com.example.dyno.VO.NowEatVO
import com.example.dyno.View.Detail.DetailMedicineActivity
import com.example.dyno.View.Detail.DetailSupplementActivity
import kotlinx.android.synthetic.main.pager_item_now_eat.view.*

class MainAdapter(private val context: Context,private val list:ArrayList<NowEatVO>):PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater=LayoutInflater.from(container.context)
        val view=inflater.inflate(R.layout.pager_item_now_eat,container,false)

        view.name.text=list[position].item_name
        view.date.text=list[position].item_date
        view.detail.text=list[position].item_list_data
        //view.detail2.text=list[position].detail2

        // 의약품일 경우 - 빨강
        if(list[position].type==1){
            view.card_back.setImageResource(R.drawable.ic_main_card_red)
            /*view.card_item.setCardBackgroundColor(ContextCompat.getColor(context,
                R.color.dynoMainBlueLight
            ))*/
        }
        // 건강기능식품일 경우 - 초록
        else if(list[position].type==2){
            view.card_back.setImageResource(R.drawable.ic_main_card_green)
            /*view.card_item.setCardBackgroundColor(ContextCompat.getColor(context,
                R.color.dynoMainBlue
            ))*/
        }

        view.setOnClickListener {
            lateinit var intent : Intent
            // 의약품일 경우
            if(list[position].type==1){
                intent = Intent(context,DetailMedicineActivity::class.java)
                intent.putExtra("DISEASE_FROM_MAIN",list[position].item_id)
            }
            // 건강기능식품일 경우
            else if(list[position].type==2){
                intent = Intent(context,DetailSupplementActivity::class.java)
                intent.putExtra("SUPPLEMENT_FROM_MAIN",list[position].item_id)
            }

            context.startActivity(intent)
        }

        //제목의 경우 약은 처방전 이름으로
        //건강기능식품은 건강기능식품 이름으로
        //세부정보로 약의 경우는 약 종류를 건강기능식품의 경우는?

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view==obj
    }

    override fun getCount(): Int {
        return list.size

    }

}