package com.example.dyno.Server

import android.util.Log
import android.util.Xml
import com.example.dyno.VO.CombineVO
import org.w3c.dom.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class ApiProc {
    fun getInfo(url: String?, key: String?, medicines: String?): String? {

        var arr = medicines?.split("/")?.toTypedArray()//병ㅇ

        var response = StringBuilder()
        val urlBuilder =
            StringBuilder("http://apis.data.go.kr/1470000/DURPrdlstInfoService/getUsjntTabooInfoList")
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + key)
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "typeName",
                "UTF-8"
            ) + "=" + URLEncoder.encode("병용금기", "UTF-8")
        )
        if (arr != null) {
            var apiResult: ArrayList<CombineVO>?=null
            for (i in arr.indices) {
                var mName:String=arr[i]
                urlBuilder.append(
                    "&" + URLEncoder.encode(
                        "itemName",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode(mName, "UTF-8")
                )
                try {
                    val url = URL(urlBuilder.toString())
                    var b_itemName: Boolean = false//같이 먹으면 안되는 의약품 이름 태그 있는지 체크용
                    var b_prohibitContent: Boolean = false//같이 먹으면 안되는 이유 태그 있는지 체크용
                    /*val con: HttpURLConnection = url.openConnection() as HttpURLConnection

                    if (con != null) {
                        Log.d("api", "Connection Success")
                        con.requestMethod = "GET"
                        con.setRequestProperty("Content-type", "application/json")
                    } else {
                        Log.d("api", "Connection Fail")
                    }

                    val responesCode: Int = con.getResponseCode()
                    var rd:BufferedReader
                    if(responesCode>=200 && responesCode<=300){
                        rd = BufferedReader(InputStreamReader(con.inputStream))

                        var line:String?=null
                        while ({line=rd.readLine(); line}()!=null) {
                            response.append(line);
                        }
                        rd.close()
                        Log.d("api","response:"+response)
                    }else{
                        Log.d("api","responseCode"+responesCode.toString())
                        Log.d("api","responseCode:"+con.getErrorStream())
                    }*/
                    val inputStream: InputStream = url.openStream()
                    val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                    val parser: XmlPullParser = factory.newPullParser()
                    parser.setInput(InputStreamReader(inputStream, "UTF-8"))

                    var items: ArrayList<CombineVO>? = null

                    val eventType: Int = parser.eventType
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        var name:String=""
                        var durReason:String=""
                        when (eventType) {
                            XmlPullParser.END_DOCUMENT->if(items!=null){
                                var item=CombineVO(mName,items)
                                apiResult?.add(item)
                            }
                            XmlPullParser.END_TAG -> if (parser.getName().equals("item")) {
                                if (name != null||durReason!=null) {
                                    var item= CombineVO(name,durReason)
                                    items?.add(item)
                                }
                            }
                            XmlPullParser.START_TAG -> if(parser.getName().equals("item")){
                                name=""
                                durReason=""
                            }
                                else if (parser.getName().equals("ITEM_NAME")) {
                                b_itemName = true
                            } else if (parser.getName().equals("PROHIBT_CONTENT")) {
                                b_prohibitContent = true
                            }
                            XmlPullParser.TEXT->if(b_itemName){
                                name=parser.text
                            }else if(b_prohibitContent){
                                durReason=parser.text
                            }

                        }
                    }


                } catch (e: Exception) {
                    Log.d("api", e.toString())
                }
            }

        }
        return response.toString()
    }


}