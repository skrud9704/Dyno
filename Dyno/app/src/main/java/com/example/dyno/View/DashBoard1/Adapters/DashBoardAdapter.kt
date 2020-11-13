package com.example.dyno.View.DashBoard1.Adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import com.example.dyno.View.DashBoard1.DashBoard1DetailActivity
import kotlinx.android.synthetic.main.recyclerlist_item_dash_board1.view.*


class DashBoardAdapter(private val context: Context, private var list:List<NotRecommendVO>):
    RecyclerView.Adapter<DashBoardAdapter.ViewHolder>(){
    private val localDB = RoomDB.getInstance(context)
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val s_in=list[position].s_ingredient
        holder.ig_name.text=s_in
        var resId:Int=getImgsrc(s_in)
        holder.ig_img.setImageBitmap(

            decodeSampledBitmapFromResource(context.resources, resId, 100, 100)
        )
        val count:Int = localDB.notRecommmendDAO().getCountforEach(s_in)
        val count_string = " ("+count+")"
        val disease_string = "["+list[position].d_name+"]의 처방전 외..."
        holder.ig_count.text = count_string
        holder.ig_dname.text=disease_string
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View =LayoutInflater.from(parent.context)
           .inflate(R.layout.recyclerlist_item_dash_board1, parent,false)
        return ViewHolder(view)

    }


    fun getImgsrc(name:String):Int{
        var resId:Int=-1
        when(name){
            "인삼" -> resId=R.drawable.ic_sample_ginseng
            "프로바이오틱스"->resId=R.drawable.ic_sample_probiotics
            "알로에"->resId=R.drawable.ic_sample_aloe
            "오메가-3 지방산"->resId=R.drawable.ic_sample_omega3
            "밀크씨슬"->resId=R.drawable.ic_sample_mt
            "감마리놀렌산"->resId=R.drawable.ic_sample_gong
            "당귀"->resId=R.drawable.ic_sample_danggui
            "마테"->resId=R.drawable.ic_sample_mate
            "돌외잎"->resId=R.drawable.ic_sample_dolwue
            "대두"->resId=R.drawable.ic_sample_big_bean
            "카르니틴"->resId=R.drawable.ic_sample_carnitine
            "녹차"->resId=R.drawable.ic_sample_greentea
            "키토산"->resId=R.drawable.ic_sample_chitosan
            "스피루리나"->resId=R.drawable.ic_sample_spirulina
            "석류"->resId=R.drawable.ic_sample_seokryu
            "공액리놀레산"->resId=R.drawable.ic_sample_cla
            "와일드망고"->resId=R.drawable.ic_sample_mango
            "클로렐라"->resId=R.drawable.ic_sample_chlorella
            "글루코사민"->resId=R.drawable.ic_sample_glucosamin
            "가시오가피"->resId=R.drawable.ic_sample_gasiogapi
            "코엔자임Q10"->resId=R.drawable.ic_sample_coenzyme
            "은행"->resId=R.drawable.ic_sample_eunhaeng
            "쏘팔메토추출물"->resId=R.drawable.ic_sample_ssopalmeto
            "크랜베리"->resId=R.drawable.ic_sample_cranberry
            "포스파티딜세린"->resId=R.drawable.ic_sample_postafatidylserine
            "감초추출물"->resId=R.drawable.ic_sample_gamcho
            "커뮤닌"->resId=R.drawable.ic_sample_ulgeum
            else->resId=R.drawable.ic_sample_mt
        }
        return resId
    }

    fun setNewData(data: List<NotRecommendVO>){
        this.list = data
        notifyDataSetChanged()
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
    fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    inner class ViewHolder (itemView: View):
        RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val ig_img = itemView.warn_s_img
        val ig_name = itemView.warn_ingredient_name
        val ig_count = itemView.warn_ingredient_count
        val ig_dname=itemView.warn_disease
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val data=list[adapterPosition].s_ingredient
            val intent = Intent(context,
                DashBoard1DetailActivity::class.java)
            intent.putExtra("DATA",data)
            context.startActivity(intent)
        }

    }

}