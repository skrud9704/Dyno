package com.example.dyno.Detail.Adapters

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
        // 기능성원재료 이름 TextView
        holder.ingredient_tv.text = ingredientModels[position]
        // 기능성내용 TextView
        holder.info_tv.text = infoModels[position]

        // 확장 버튼 클릭 시 = 접힌 아이템을 펼칠 때
        /*holder.expand_btn.setOnClickListener {
            // 버튼 바꿔치기
            holder.expand_btn.visibility = View.GONE
            holder.fold_btn.visibility = View.VISIBLE

            /*
            // 펼쳐진 아이템 클릭시
            if(selectedItems.get(position)){
                selectedItems.delete(position)
            }
            // 접힌 아이템 클릭시
            else{
                // 직전의 클릭했던 item의 클릭상태를 지움
                selectedItems.delete(prePosition)
                // 클릭한 item의 position 저장
                selectedItems.put(position,true)
            }
            // 해당 포지션의 변화를 알림
            if(prePosition!=-1)
                notifyItemChanged(prePosition)
            notifyItemChanged(position)
            // 클릭된 position 저장
            prePosition = position*/

            changeVisibility(true,holder)
        }

        // 접기 버튼 클릭 시 = 확장된 아이템을 접을 때
        holder.fold_btn.setOnClickListener {
            // 버튼 바꿔치기
            holder.fold_btn.visibility = View.GONE
            holder.expand_btn.visibility = View.VISIBLE


            changeVisibility(false,holder)
        }*/
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Expandable한 Item을 위해 기능성내용의 Visibility를 조정하는 함수.
    private fun changeVisibility(isExpanded : Boolean, holder: ViewHolder){
        val dpValue=holder.info_layout.layoutParams.height
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
            val value = (it.animatedValue) as Int
            holder.info_layout.layoutParams.height = value
            holder.info_layout.requestLayout()
            if(isExpanded){
                holder.info_layout.visibility = View.VISIBLE
            }
            else {
                holder.info_layout.visibility = View.GONE
            }
        }
        va.start()
    }


    // 커스텀 ViewHolder, 리사이클러뷰의 각각의 아이템마다 데이터를 넣을 View 를 미리 선언하고 가져옴, 클릭리스너 implement
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ingredient_tv : TextView
        var info_tv : TextView
        var expand_btn : ImageView
        var fold_btn : ImageView
        var info_layout : LinearLayout

        init{
            ingredient_tv = itemView.ingredient_name
            info_tv = itemView.ingredient_info
            expand_btn = itemView.expand_btn
            fold_btn = itemView.fold_btn
            info_layout = itemView.s_info_layout
        }

    }

}

//Expandable 참고 : https://dev-imaec.tistory.com/30