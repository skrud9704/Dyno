package com.example.dyno.RegistSupplement

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
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

var siteSource : String =""

class RegistSupplementActivity : AppCompatActivity() {
    // 식품안전나라 건강기능식품 검색 주소
    val siteUrl : String = "https://www.foodsafetykorea.go.kr/portal/healthyfoodlife/searchHomeHF.do?menu_grp=MENU_NEW01&menu_no=2823"
    val TAG = "Jsoup"
    var keyword : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_supplement)

        //1. 웹뷰 초기셋팅
        webViewConfiguration()
        btn_search.setOnClickListener {
            webView.loadUrl(siteUrl)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webViewConfiguration(){
        val myJavaScriptInterface = MyJavaScriptInterface()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.addJavascriptInterface(myJavaScriptInterface,"Dyno")
        webView.webViewClient = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                val js = "javascript:document.getElementById('search_word').value=\"$keyword\";"+
                        "javascript:fn_search();"
                view?.evaluateJavascript(js){ value ->
                    view.loadUrl(js)
                }
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                keyword = input_search.text.toString()
                Log.d("he",keyword)
                //view?.loadUrl("javascript:document.getElementById('search_word').value=\"바나나\";")

                val js = "javascript:document.getElementById('search_word').value=\"$keyword\";"+
                        "javascript:fn_search();"

                view?.evaluateJavascript(js){ value ->
                    //view.loadUrl(js)
                    view.loadUrl("javascript:fn_search();")

                }

            }

            /*override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                view?.evaluateJavascript(""){ value ->
                    //view.loadUrl(js)
                    view.loadUrl("javascript:fn_search();")
                }
            }*/

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                view?.evaluateJavascript(""){ value ->
                    //view.loadUrl(js)
                    view.loadUrl("javascript:fn_search();")
                    view?.loadUrl("javascript:window.Dyno.getHtml(document.getElementsByTagName('tbody')[0].innerHTML);")
                }
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class CrawlingAsyncTask : AsyncTask<String, String, String>(){
        private var result : String = ""
        var supplementList: ArrayList<SupplementVO> = arrayListOf()

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            webViewConfiguration()
        }
        override fun doInBackground(vararg params: String?): String? {
            webView.loadUrl(siteUrl)
            return null

            //val doc : Document = Jsoup.connect("$siteUrl").get()
            //val elts: Elements = doc.select("")
            // val eltsSize = elts.size
            // Log.d(TAG, eltsSize.toString())
            /*elts.forEachIndexed { index, elem ->
                val a_href = elem.select("a").attr("href")
                val title = elem.select("").text()
                Log.d(TAG,"$index $a_href")
                //supplementList.add()
            }
            return doc.title()*/
        }

        override fun onPostExecute(result: String?) {
            progressBar.visibility = View.GONE
            // 결과 출력 및 어답터 설정.
        }

    }

    class MyJavaScriptInterface() {
        @JavascriptInterface
        fun getHtml(html : String) {
            siteSource = html;
            Log.d("hello","나 호출은 됐다.")
            Log.d("hello",siteSource)
        }
    }

}