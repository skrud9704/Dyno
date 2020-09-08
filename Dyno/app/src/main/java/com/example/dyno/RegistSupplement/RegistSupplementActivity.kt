package com.example.dyno.RegistSupplement

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_regist_supplement.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

// process
// 0. 이름으로 검색.
// 1. RDS에 해당 건강기능 식품이 존재하는지 검사
// 2. 1.에서 있다면 바로 등록.
// 3. 1.에서 없다면 웹에서 크롤링 후 RDS 저장, 어플에서 등록.
// 일단, RDS 절차 건너뛰고 3부터.

class RegistSupplementActivity : AppCompatActivity() {
    // 식품안전나라 건강기능식품 검색 주소
    val siteUrl : String = "https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/searchHomeHF.do?menu_grp=MENU_NEW01&menu_no=2823&menu_no=2823&menu_grp=MENU_NEW01"
    val TAG = "Jsoup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)

        btn_search.setOnClickListener {
            // 네트워크 작업 Async Task
            CrawlingAsyncTask().execute(siteUrl)
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class CrawlingAsyncTask : AsyncTask<String, String, String>(){
        private var result : String = ""
        var supplementList: ArrayList<SupplementVO> = arrayListOf()

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }
        override fun doInBackground(vararg params: String?): String {
            val doc : Document = Jsoup.connect("$siteUrl").get()
            val elts: Elements = doc.select("")
            // val eltsSize = elts.size
            // Log.d(TAG, eltsSize.toString())
            elts.forEachIndexed { index, elem ->
                val a_href = elem.select("a").attr("href")
                val title = elem.select("").text()
                Log.d(TAG,"$index $a_href")
                //supplementList.add()
            }
            return doc.title()
        }

        override fun onPostExecute(result: String?) {
            progressBar.visibility = View.GONE

            // 결과 출력 및 어답터 설정.
        }

    }

}