package com.example.dyno.Network.PublicAPI

import android.util.Log
import com.example.dyno.VO.CombineVO
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder

class ApiProc {
    fun getInfo(url: String?, key: String?, medicines: String?): String? {

        var arr = medicines?.split("/")?.toTypedArray()//병ㅇ
        Log.d("api", "arr:" + arr)

        var response = StringBuilder()
        if (arr != null) {
            var apiResult :String=""
            for (i in arr.indices) {
                var mName: String = arr[i]
                val urlBuilder =
                    StringBuilder("http://apis.data.go.kr/1470000/DURPrdlstInfoService/getUsjntTabooInfoList")
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + key)
                urlBuilder.append(
                    "&" + URLEncoder.encode(
                        "typeName",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode("병용금기", "UTF-8")
                )
                urlBuilder.append(
                    "&" + URLEncoder.encode(
                        "itemName",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode(mName, "UTF-8")
                )
                Log.d("api", "urlBuilder" + urlBuilder.toString())
                apiResult+=mName
                apiResult+="@"
                try {
                    val url = URL(urlBuilder.toString())
                    var b_itemName: Boolean = false//같이 먹으면 안되는 의약품 이름 태그 있는지 체크용
                    var b_prohibitContent: Boolean = false//같이 먹으면 안되는 이유 태그 있는지 체크용
                    var b_resultNum: Boolean = false//병용 금기인 의약품 수
                    var resultNum: String = "0"
                    /*val con: HttpURLConnection = url.openConnection() as HttpURLConnection

                    if (con != null) {
                        Log.d("api", "Connection Success")
                        con.requestMethod = "GET"
                        con.setRequestProperty("Content-type", "application/json")
                    } else {
                        Log.d("api", "Connection Fail")
                    }

                    val responesCode: Int = con.getResponseCode()
                    var rd: BufferedReader
                    if (responesCode >= 200 && responesCode <= 300) {
                        rd = BufferedReader(InputStreamReader(con.inputStream))

                        var line: String? = null
                        while ({ line = rd.readLine(); line }() != null) {
                            response.append(line);
                        }
                        rd.close()
                        Log.d("api", "response:" + response)
                        var item = CombineVO(mName, response.toString())
                        apiResult.add(item)
                    } else {
                        Log.d("api", "responseCode" + responesCode.toString())
                        Log.d("api", "responseCode:" + con.getErrorStream())
                    }*/
                    var inputStream: InputStream = url.openStream()
                    val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()

                    val parser: XmlPullParser = factory.newPullParser()
                    parser.setInput(InputStreamReader(inputStream, "UTF-8"))

                    var items :String=""

                    var eventType: Int = parser.eventType
                    Log.d("api","eventType:"+eventType)
                    out@ while (eventType != XmlPullParser.END_DOCUMENT ) {
                        var name: String = ""
                        var durReason: String = ""
                        Log.d("api","eventTypeW:"+eventType)
                        when (eventType) {
                            XmlPullParser.END_TAG -> if (parser.getName().equals("item")) {
                                if (name != null || durReason != null) {

                                    items=name+"#"+durReason

                                    apiResult+=items
                                    apiResult+="$"

                                    Log.d("api", "item:" + items)
                                    Log.d("api", "apiResult:" + apiResult)
                                }
                            }else if(parser.getName().equals("items")){
                                apiResult+="%"
                            }
                            XmlPullParser.START_TAG -> if (parser.getName().equals("totalCount")) {
                                b_resultNum = true
                            } else if (parser.getName().equals("items")) {
                                Log.d("api","items")
                                name = ""
                                durReason = ""
                            } else if (parser.getName().equals("ITEM_NAME")) {
                                b_itemName = true
                            } else if (parser.getName().equals("PROHIBT_CONTENT")) {
                                b_prohibitContent = true
                            }
                            XmlPullParser.TEXT -> if (b_resultNum) {
                                resultNum = parser.text
                                Log.d("api", "totalCount" + resultNum)
                                if (resultNum == "0") {
                                    apiResult+="0"
                                    apiResult+="$"
                                    apiResult+="%"
                                    Log.d("api","break할거임")
                                    break@out
                                }
                            } else if (b_itemName) {
                                name = parser.text
                                Log.d("api", "xmlText:name" + name)
                            } else if (b_prohibitContent) {
                                durReason = parser.text
                                Log.d("api", "xmlText:durReason" + durReason)
                            }

                        }
                        eventType=parser.next()
                    }

                } catch (e: Exception) {
                    Log.d("api", "error" + e.toString())
                }
            }
            return apiResult
            Log.d("api", "apiResult" + apiResult)
        }
        return response.toString()
    }


}