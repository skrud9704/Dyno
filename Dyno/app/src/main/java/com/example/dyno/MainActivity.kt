package com.example.dyno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.dyno.Retrofit2.RetrofitClient
import com.example.dyno.Retrofit2.RetrofitService
import com.example.dyno.VO.UserVO
import kotlinx.android.synthetic.main.activity_main.*
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitClient: RetrofitClient = RetrofitClient()

        var inputid = findViewById(R.id.id) as EditText

        button.setOnClickListener {
            var id: String = inputid.text.toString()

            retrofitClient.buildRetrofit().requestJoin(id)

        }
    }
}
