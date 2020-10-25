package com.example.dyno.OCR

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.dyno.R
import com.example.dyno.View.MyPage.RegistMedicine.RegistMedicineActivity
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class
CameraActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // 의약품 등록일 경우 (From MainActivity)
        if(intent.getStringExtra("DATA")=="medicine"){
            regist_medicine.visibility = View.VISIBLE
            regist_supplement.visibility = View.GONE
        }
        // 건강기능식품 등록일 경우 (From RegistSupplementActivity)
        else if(intent.getStringExtra("DATA")=="supplement"){
            regist_medicine.visibility = View.GONE
            regist_supplement.visibility = View.VISIBLE
        }

        // 카메라 권한 획득
        setPermission()

        // 촬영 버튼 클릭 시
        btn_picture.setOnClickListener {
            startCapture()
        }
        // 재촬영 버튼 클릭 시
        btn_repicture.setOnClickListener {
            startCapture()
        }

        startOCR.setOnClickListener{
            val nextIntent=Intent(this, RegistMedicineActivity::class.java)
            nextIntent.putExtra("bitmapImg",currentPhotoPath)
            startActivity(nextIntent)
            finish()
        }
    }

    fun setPermission(){
        val permission = object : PermissionListener{
            override fun onPermissionGranted() {
                Toast.makeText(this@CameraActivity,"권한허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(this@CameraActivity,"권한 거부",Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()

    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile() : File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }


    private fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.dyno",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)
            before_picture.visibility=View.GONE
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))
                img_picture.setImageBitmap(bitmap)
            }
            else{
                val decode = ImageDecoder.createSource(this.contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
                img_picture.setImageBitmap(bitmap)
            }
            img_picture.visibility = View.VISIBLE
        }
        return
    }
}
