package com.example.dyno.Detail.Adapters

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.Detail.Adapters.DetailSAdapter.ViewHolder
import com.example.dyno.R
import kotlinx.android.synthetic.main.recyclerlist_item_expandable_supplement.view.*

class DetailSAdapter(context: Context, ingredients : ArrayList<String>, infos : ArrayList<String>)
    : RecyclerView.Adapter<ViewHolder>() {

    // 한 건강기능식품에 들어있는 기능성원재료 정보
    var ingredientModels: ArrayList<String>
    // 각 기능성원재료들에 대한 기능성내용
    var infoModels: ArrayList<String>
    // context
    var context: Context

    // Expandable한 UI를 위해 필요한 변수
    // 1. Item의 클릭 상태를 저장할 배열
    var selectedItems : SparseBooleanArray = SparseBooleanArray()
    // 2. 직전에 클릭했던 item의 position
    var prePosition = -1

    // 초기화. (init을 지우고 위에 맴버변수에 바로 할당해도 됨.)
    init {
        this.ingredientModels = ingredients
        this.context = context
        this.infoModels = infos
    }

    // 아래에 inner class로 커스텀 ViewHolder 만듬. -> implement 함.
    // 리사이클러뷰 아이템 하나에 대한 layout을 inflate해서 커스텀ViewHolder를 생성해 리턴하는 것.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.recyclerlist_item_expandable_supplement, parent, false)
        return ViewHolder(view)
    }

    // 카운트를 리턴하지 않으면 onCreateViewHolder를 부르지 않음. 즉 리턴된 카운트 수만큼 onCreateViewHolder를 부름.
    override fun getItemCount(): Int {
        return ingredientModels.size
    }

    // Data 와 커스텀 ViewHolder를 Bind하는 부분.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredient_tv.text = ingredientModels[position]
        holder.info_tv.text = infoModels[position]
        holder.expand_btn.setOnClickListener {
            if(selectedItems.get(position)){
                selectedItems.delete(position)
            }else{
                selectedItems.delete(prePosition)
                selectedItems.put(position,true)
            }

            if(prePosition!=-1)
                notifyItemChanged(prePosition)
            notifyItemChanged(position)

            prePosition = position
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun changeVisibility(isExpanded : Boolean, holder: ViewHolder){
        val dpValue=150
        val d = context.resources.displayMetrics.density
        val height = (dpValue*d).toInt()
        var va : ValueAnimator = ValueAnimator()
        if(isExpanded){
            va = ValueAnimator.ofInt(0,height)
        }else{
            va = ValueAnimator.ofInt(height,0)
        }

        va.duration=600
        va.addUpdateListener {

        }

        //va.start()
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ingredient_tv : TextView
        var info_tv : TextView
        var expand_btn : ImageView

        init{
            ingredient_tv = itemView.ingredient_name
            info_tv = itemView.ingredient_info
            expand_btn = itemView.expand_btn
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }

}

//Expandable 참고 : https://dev-imaec.tistory.com/30