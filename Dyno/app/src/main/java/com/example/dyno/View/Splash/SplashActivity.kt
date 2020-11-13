package com.example.dyno.View.Splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.VO.DynoDurSupplementVO
import com.example.dyno.View.Main.MainActivity
import retrofit2.Call
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    var SHARED_PREF = "SaveLogin"
    var SHARED_PREF_NAME = "name"
    var SHARED_PREF_DID = "did"
    private var dynoDurSupplementVO:DynoDurSupplementVO= DynoDurSupplementVO()
    var TAG="splash"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(2000)
        // 앱에 디바이스 아이디와 회원 명이 이미 저장돼있다면
        // -> 메인 액티비티
        // 그렇지 않다면
        // -> SignUp 액티비티

        val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val prefName = pref.getString(SHARED_PREF_NAME,"null")
        val prefDid = pref.getString(SHARED_PREF_DID,"null")

        val count = getCount()
        getSupplementDur(count)
        if(prefDid=="null" && prefName=="null"){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }else{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
    private fun getSupplementDur(id:Int){
        val retrofit = RetrofitClient.getInstance()
        val notRecommendServer = retrofit.create(RetrofitService::class.java)

        notRecommendServer.requestDynoDurSupplement(id).enqueue(object : retrofit2.Callback<ArrayList<DynoDurSupplementVO>>{
            override fun onFailure(call: Call<ArrayList<DynoDurSupplementVO>>, t: Throwable) {
                Log.d(TAG,"dyno dur suppplement 가져오는거 실패{$t}")
                Log.d(TAG,"dyno dur suppplement 가져오는거 실패{$call}")
            }

            override fun onResponse(
                call: Call<ArrayList<DynoDurSupplementVO>>,
                response: Response<ArrayList<DynoDurSupplementVO>>
            ) {
                Log.d(TAG,"dyno dur supplement 가져오는거 성공")
                for(item in response.body()!!){
                    dynoDurSupplementVO=item
                    insertLocalDB()
                }
            }
        })
    }

    private fun insertLocalDB(){
        Log.d(TAG,"RoomDB 접근")
        // DB 싱글톤으로 생성.
        val localDB = RoomDB.getInstance(this)
        localDB.durSupplementDAO().insertDynoDurSupplement(dynoDurSupplementVO)
        Toast.makeText(this,"dyno dur supplement 추가.", Toast.LENGTH_SHORT).show()

        // DB 닫기.
        RoomDB.destroyInstance()
    }
    private fun getCount():Int{
        val localDB = RoomDB.getInstance(this)
        val count = localDB.durSupplementDAO().getLocalDurCount()
        RoomDB.destroyInstance()
        return count
    }
}