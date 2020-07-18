package com.example.dyno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.dyno.Retrofit2.RetrofitClient
import com.example.dyno.Retrofit2.RetrofitService
import com.example.dyno.VO.UserVO
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitClient: RetrofitClient = RetrofitClient()

        val inputid = findViewById(R.id.id) as EditText

        button.setOnClickListener {
            val id: String = inputid.text.toString()

            retrofitClient.buildRetrofit().requestJoin(id).enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("db insert", "실패"+t.localizedMessage)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("db insert", "성공"+response?.body().toString())
                }


            })

        }
    }
}
