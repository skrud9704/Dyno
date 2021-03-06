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
            decodeSampledBitmapFromResource(resources, getImgsrc(key), 100, 100)
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
}