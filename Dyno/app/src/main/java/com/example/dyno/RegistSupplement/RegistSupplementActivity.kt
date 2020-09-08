package com.example.dyno.RegistSupplement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dyno.R
import org.jsoup.Jsoup

// process
// 0. 이름으로 검색.
// 1. RDS에 해당 건강기능 식품이 존재하는지 검사
// 2. 1.에서 있다면 바로 등록.
// 3. 1.에서 없다면 웹에서 크롤링 후 RDS 저장, 어플에서 등록.
// 일단, RDS 절차 건너뛰고 3부터.

class RegistSupplementActivity : AppCompatActivity() {
    // 웹 크롤링 주소
    private var doc = Jsoup.connect("https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/searchHomeHF.do?menu_grp=MENU_NEW01&menu_no=2823").get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)

    }
}