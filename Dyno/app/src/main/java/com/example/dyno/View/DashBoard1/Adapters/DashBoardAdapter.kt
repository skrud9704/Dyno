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


class DashBoardAdapter(private val context: Context, private val list:List<NotRecommendVO>):
    RecyclerView.Adapter<DashBoardAdapter.ViewHolder>(){
    private val localDB = RoomDB.getInstance(context)
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ig_img.setImageBitmap(
            decodeSampledBitmapFromResource(context.resources, R.drawable.ic_sample_mt, 100, 100)
        )
        val s_in=list[position].s_ingredient
        holder.ig_name.text=s_in
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