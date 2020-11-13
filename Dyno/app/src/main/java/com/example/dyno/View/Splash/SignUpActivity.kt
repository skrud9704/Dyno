package com.example.dyno.View.Splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.dyno.LocalDB.RoomDB
import com.example.dyno.Network.RetrofitClient
import com.example.dyno.Network.RetrofitService
import com.example.dyno.VO.UserVO
import com.example.dyno.View.Main.MainActivity
import com.example.dyno.R
import com.example.dyno.VO.DynoDurSupplementVO
import com.example.dyno.VO.NotRecommendVO
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var mLocalDatabase : FirebaseDatabase
    private lateinit var mRef : DatabaseReference

    private val SHARED_PREF = "SaveLogin"
    private val SHARED_PREF_NAME = "name"
    private val SHARED_PREF_DID = "did"

    private val TABLE_NAME = "USERS"

    private var isConsent : Boolean = false

    private val TAG = this::class.java.simpleName

    private var dynoDurSupplementVO: DynoDurSupplementVO = DynoDurSupplementVO()


    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // 사용자 명 받을 변수 준비
        lateinit var userName :String
        // 기기 아이디 보여주기
        val deviceId = Settings.Secure.getString(this.contentResolver,Settings.Secure.ANDROID_ID)
        userD.setText(deviceId)
        userD.isEnabled=false

        // 로그 확인.
        Log.d("device info",deviceId)

        consent.setOnCheckedChangeListener { _, isChecked ->
            isConsent = isChecked
        }

        // 가입 버튼 클릭 시
        button.setOnClickListener{
            if(isConsent){
                // 1. 사용자 명 저장.
                userName = userN.text.toString()

                // 2. 사용자 객체 생성
                val newUser = UserVO(deviceId, userName)

                // 3. 파이어베이스 데이터 삽입
                // 노드 형태 : "USERS"(테이블 명) 아래 "Device ID"(한 데이터의 Key) 아래 "User Name"(그 데이터 아래 key 아닌 값)
                insertUserData(newUser)

                // 4. 자동 로그인을 위한 sharedPreference
                // 나중에 앱 재시작 시 -> 두 값(SHARED_PREF_NAME, SHARED_PREF_DID)이 ""이 아니면 현재 액티비티(SignUpActivity.kt)는 건너뜀.
                val pref : SharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                val editor = pref.edit()

                editor.putString(SHARED_PREF_NAME,newUser.userName)
                editor.putString(SHARED_PREF_DID,newUser.deviceId)
                editor.apply()

                // 5. 메인 액티비티로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"약관에 동의하셔야 합니다.", Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun insertUserData(user : UserVO){
        mLocalDatabase = FirebaseDatabase.getInstance()
        mRef = mLocalDatabase.getReference(TABLE_NAME)  // "USERS"
        mRef.child(user.deviceId).setValue(user)        //   ㄴ "디바이스아이디"(노드 키)
                                                        //          ㄴ "디바이스 아이디"
                                                        //          ㄴ "사용자 명"
    }

    private fun getSupplementDur(){
        val retrofit = RetrofitClient.getInstance()
        val notRecommendServer = retrofit.create(RetrofitService::class.java)

        notRecommendServer.requestDynoDurSupplement(0).enqueue(object : retrofit2.Callback<DynoDurSupplementVO>{
            override fun onFailure(call: Call<DynoDurSupplementVO>, t: Throwable) {
                Log.d(TAG,"dyno dur suppplement 가져오는거 실패{$t}")
                Log.d(TAG,"dyno dur suppplement 가져오는거 실패{$call}")
            }

            override fun onResponse(
                call: Call<DynoDurSupplementVO>,
                response: Response<DynoDurSupplementVO>
            ) {
                Log.d(TAG,"dyno dur supplement 가져오는거 성공")
                dynoDurSupplementVO=response.body()!!
                insertLocalDB()
            }
        })
    }

    private fun insertLocalDB(){
        Log.d(TAG,"RoomDB 접근")
        // DB 싱글톤으로 생성.
        val localDB = RoomDB.getInstance(this)
        localDB.durSupplementDAO().insertDynoDurSupplement(dynoDurSupplementVO)
        Toast.makeText(this,"dyno dur supplement 추가.",Toast.LENGTH_SHORT).show()

        // DB 닫기.
        RoomDB.destroyInstance()
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<DynoDurSupplementVO>) {

}


