package com.example.dyno

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class OcrProc {
    fun start(ocrApiUrl: String, ocrSecretKey: String): String? {
        var ocrMessage = ""
        try {
            /*val objectStorageURL =
                "https://kr.object.ncloudstorage.com/labs/ocr/ocr_sample.jpg"

            val message = getReqMessage(objectStorageURL)
            println("##$message")*/
            val url = URL(ocrApiUrl)
            val apiURL = ocrApiUrl
            val timestamp: Long = Date().getTime()
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.setRequestMethod("POST")
            con.setRequestProperty("Content-Type", "application/json;UTF-8")
            con.setRequestProperty("X-OCR-SECRET", ocrSecretKey)

            //예제로 drawable에 저장한 사진데이터를 사용하여 json으로 만든다음 보냄
           val jObject:JSONObject
            jObject.put("version","v1")
            jObject.put("requestId","sampleid")
            jObject.put("timestamp",timestamp)
            val jArray:JSONArray
            jArray.put("name","sample_image")
            jArray.put("format","png")
            jArray.put("data",)

            // post request
           /*con.setDoOutput(true)
            val wr = DataOutputStream(con.getOutputStream())
                    wr.write(message.toByteArray(charset("UTF-8")))
            wr.flush()
            wr.close()*/

            val responseCode: Int = con.getResponseCode()

            if (responseCode == 200) { // 정상 호출
                System.out.println(con.getResponseMessage())
                val `in` = BufferedReader(
                    InputStreamReader(
                        con.getInputStream()
                    )
                )
                var decodedString: String
                while (`in`.readLine().also({ decodedString = it }) != null) {
                    ocrMessage = decodedString
                    //chatbotMessage = decodedString;
                }
                `in`.close()
            } else {  // 에러 발생
                ocrMessage = con.getResponseMessage()
            }
        } catch (e: Exception) {
            println(e)
        }
        println(">>>>>>>>>>$ocrMessage")
        return ocrMessage
    }

    fun getReqMessage(objectStorageURL: String?): String {
        var requestBody = ""
        try {
            val timestamp: Long = Date().getTime()
            val json = JSONObject()
            json.put("version", "V1")
            json.put("requestId", UUID.randomUUID().toString())
            json.put("timestamp", java.lang.Long.toString(timestamp))
            val image = JSONObject()
            image.put("format", "jpg")
            image.put("url", objectStorageURL)
            image.put("name", "test_ocr")
            val images = JSONArray()
            images.put(image)
            json.put("images", images)
            requestBody = json.toString()
        } catch (e: Exception) {
            println("## Exception : $e")
        }
        return requestBody
    }
}
