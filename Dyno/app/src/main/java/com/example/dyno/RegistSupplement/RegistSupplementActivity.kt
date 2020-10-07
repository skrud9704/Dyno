package com.example.dyno.RegistSupplement

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.dyno.MyPage.MyPageActivity
import com.example.dyno.R
import com.example.dyno.VO.SupplementVO
import kotlinx.android.synthetic.main.activity_regist_supplement.*

// process
// 0. 이름으로 검색.
// 1. RDS에 해당 건강기능 식품이 존재하는지 검사
// 2. 1.에서 있다면 바로 등록.
// 3. 1.에서 없다면 웹에서 크롤링 후 RDS 저장, 어플에서 등록.
// 일단, RDS 절차 건너뛰고 3부터.

var siteSource : String =""

class RegistSupplementActivity : AppCompatActivity() {
    // 식품안전나라 건강기능식품 검색 주소
    val siteUrl : String = "https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/searchHomeHF.do?menu_grp=MENU_NEW01&menu_no=2823"
    //val TAG = "Jsoup"
    var keyword : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)
        //1. 웹뷰 초기셋팅
        //webViewConfiguration()
        btn_search.setOnClickListener {
            //webView.loadUrl(siteUrl)
            if(input_search.text.toString()=="인삼차"){
                no_result.visibility = View.GONE
                search_list.visibility=View.VISIBLE
                search_list.adapter=SupplementAdapter(this,R.layout.list_item_search_supplement,ArrayList<SupplementVO>())
            }else{
                no_result.visibility = View.VISIBLE
                search_list.visibility=View.GONE
            }
        }

        search_list.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MyPageActivity::class.java)
            intent.putExtra("ver","demo")
            startActivity(intent)
            finish()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webViewConfiguration(){
        val myJavaScriptInterface = MyJavaScriptInterface()

        // Javascript 사용하기
        webView.settings.javaScriptEnabled = true
        // DOM 사용하기
        webView.settings.domStorageEnabled = true
        // 캐시 사용모드 - LOAD_NO_CACHE : 항상 최신 데이터 불러옴.
        //webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        // 커스텀 인터페이스 추가 -> ex) document.android.Dyno
        webView.addJavascriptInterface(myJavaScriptInterface,"Dyno")
        webView.webViewClient = object : WebViewClient(){

            /* 페이지가 로드되기 시작할 때부터 동작
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }*/

            // 페이지가 완전히 로드 된 후에 동작
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                keyword = input_search.text.toString()
                Log.d("검색어",keyword)

                val js = "javascript:document.getElementById('search_word').value=\"$keyword\"; javascript:fn_search();"
                val js2 = "javascript:android.Dyno.getHtml()"
                view?.evaluateJavascript(js){ value ->
                    Log.d("js",value)
                }
            }

        }
        webView.loadUrl(siteUrl)
    }

    class MyJavaScriptInterface() {
        // 커스텀 메서드 마다 JavascriptInterface 어노테이션 부여해야함.
        @JavascriptInterface
        fun getHtml(html : String) {
            siteSource = html;
            Log.d("hello","나 호출 됐다.")
            Log.d("hello",siteSource)
        }
    }


}