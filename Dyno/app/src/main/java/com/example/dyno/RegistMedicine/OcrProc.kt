package com.example.dyno.RegistMedicine

import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*


class OcrProc {
    fun start(ocrApiUrl: String?, ocrSecretKey: String?,filePath:String?): String? {
        Log.d("ocr_star",ocrApiUrl)
        Log.d("ocr_filepath",filePath)
        var ocrMessage = ""
        var imgData=convertImageFileToBase64(filePath)
        Log.d("convertSuccess",imgData)
        try {
            /*val myDrawable: Drawable = getResources().getDrawable(R.drawable)
            val anImage = (myDrawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            val imageEncoded =
                Base64.encodeToString(b, Base64.DEFAULT)*/

            val message = getReqMessage(imgData)
            println("##$message")
            val arr=message.toByteArray(Charset.defaultCharset())
            val url = URL(ocrApiUrl)
            val timestamp: Long = Date().getTime()
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            if(con!=null){//연결됨
                con.requestMethod="POST"
                con.setRequestProperty("Content-Type", "application/json")
                con.setRequestProperty("X-OCR-SECRET", ocrSecretKey)
            }else{
                Log.d("con","connectionFail")
            }

            con.doOutput=true//POST로 데이터를 넘겨주겠다는 옵션
            // post request
            val wr = DataOutputStream(con.outputStream)
            wr.write(arr)
            wr.flush()
            wr.close()

            val responseCode: Int = con.getResponseCode()
            Log.d("responesCode",responseCode.toString())
            if (responseCode == 200) { // 정상 호출
                /*System.out.println(con.getResponseMessage())*/
                Log.d("trans_start",con.responseMessage)
                val input = BufferedReader(
                    InputStreamReader(con.inputStream,"utf-8")
                )
                var inputLine: String?=null
                val response=StringBuffer()
               while ({inputLine=input.readLine();inputLine}()!=null){
                   response.append(inputLine)
               }
                input.close()
                return response.toString()
            } else {  // 에러 발생
                Log.d("responseCode",responseCode.toString())
                ocrMessage = con.getResponseMessage()
            }
        } catch (e: Exception) {
            println(e)
            Log.d("error",e.toString())
        }
        Log.d("ocrMessage",ocrMessage)
        return ocrMessage
    }

    fun getReqMessage(objectStorageURL: String): String {
        var requestBody = ""
        try {
            val timestamp: Long = Date().getTime()
            val json = JSONObject()
            json.put("version", "V1")
            json.put("requestId", UUID.randomUUID().toString())
            json.put("timestamp", java.lang.Long.toString(timestamp))
            val image = JSONObject()
            image.put("format", "jpg")
            image.put("data", objectStorageURL)
            image.put("name", "test_ocr")
            val images = JSONArray()
            images.put(image)
            json.put("images", images)
            requestBody = json.toString()
        } catch (e: Exception) {
            Log.d("get_reqM_error",e.toString())

        }
        return requestBody
    }
    fun convertImageFileToBase64(imageFile: String?): String {
        Log.d("convert",imageFile)
        return FileInputStream(imageFile).use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                    inputStream.copyTo(base64FilterStream)
                    base64FilterStream.flush()
                    outputStream.toString()
                }
            }
        }
    }
}
