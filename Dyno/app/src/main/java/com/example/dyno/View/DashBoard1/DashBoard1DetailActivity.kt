package com.example.dyno.View.DashBoard1

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.R
import com.example.dyno.VO.NotRecommendVO
import com.example.dyno.View.DashBoard1.Adapters.DetailAdapter
import kotlinx.android.synthetic.main.activity_dash_board1_detail.*

class DashBoard1DetailActivity : AppCompatActivity(){
    private lateinit var notRecommentList : List<NotRecommendVO>
    private lateinit var key : String
    private lateinit var adapter : DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board1_detail)

        getData()

        setList()
    }

    private fun getData(){
        if(intent.hasExtra("DATA")){
            key = intent.getStringExtra("DATA")
            val localDB = RoomDB.getInstance(this)
            notRecommentList = localDB.notRecommmendDAO().getNotRecommend(key)!!
            RoomDB.destroyInstance()
            warn_detail_s_name.text = key

        }
    }


    private fun setList(){
        adapter  = DetailAdapter(this,notRecommentList)
        adapter.setListener(object : OnItemDeleteClickListener {
            override fun onClick(id: String) {
                deleteAlertDialog(id)
            }
        })
        warn_detail_d_list.adapter = adapter

        warn_detail_s_image.setImageBitmap(
            decodeSampledBitmapFromResource(resources, R.drawable.ic_sample_mt, 100, 100)
        )
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
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

    private fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
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

    private fun deleteAlertDialog(id:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("삭제 확인")
            .setMessage("해당 질병을 삭제하면 다른 건강기능식품 성분주의 리스트에도 삭제됩니다. 질병기록은 남아있습니다. 삭제하시겠습니까?")
            .setPositiveButton("예") { _, _ ->  deleteDisease(id)}
            .setNegativeButton("아니오"){ _, _ -> }

        val alertDialog =builder.create()
        alertDialog.show()
    }

    private fun deleteDisease(id : String){
        val localDB = RoomDB.getInstance(this)
        localDB.notRecommmendDAO().deleteDiseaseFromRecommend(id)
        val newData = localDB.notRecommmendDAO().getNotRecommend(key)!!
        adapter.setNewData(newData)
    }

    interface OnItemDeleteClickListener{
        fun onClick(id : String)
    }
}