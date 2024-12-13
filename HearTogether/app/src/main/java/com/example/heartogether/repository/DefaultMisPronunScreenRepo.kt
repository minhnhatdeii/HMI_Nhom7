package com.example.heartogether.repository

import android.content.Context
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import com.example.heartogether.data.network.ResponseMisPronun
import com.example.heartogether.data.network.ResponsePostRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DefaultMisPronunScreenRepo : MisProNunRepo {
    //tts
    override suspend fun callSpeechToText(filePath: String): String {
        val client = OkHttpClient()
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("File không tồn tại tại đường dẫn: ${file.absolutePath}")
        }
        val mediaType =
            "multipart/form-data; boundary=---011000010111000001101001".toMediaTypeOrNull()
        val fileRequestBody = RequestBody.create(mediaType, file)

// Tạo MultipartBody
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM) // multipart/form-data
            .addFormDataPart("file", file.name, fileRequestBody) // Tệp âm thanh
            //.addFormDataPart("type", "RAPID") // Tham số 'type'
            //.addFormDataPart("response_format", "JSON") // Tham số 'response_format'
            .build()

// Tạo Request
        val request = Request.Builder()
            .url("https://whisper-speech-to-text1.p.rapidapi.com/speech-to-text")
            .post(requestBody)
            .addHeader("x-rapidapi-key", "c9ff25f670mshce2c1a9f5425b01p102f39jsn0af8bcc9c506")
            .addHeader("x-rapidapi-host", "whisper-speech-to-text1.p.rapidapi.com")
            .addHeader("Content-Type", "multipart/form-data; boundary=---011000010111000001101001")
            .build()


        try {
            val response: Response = client.newCall(request).execute()
            Log.d("STT2", "${response.isSuccessful}")


            if (response.isSuccessful) {
                // Đọc body response
                val responseBody = response.body?.string()
                Log.d("STT2", "${responseBody}")

                // Chuyển đổi body thành JSONObject để truy xuất các giá trị
                val jsonResponse = JSONObject(responseBody!!)
                Log.d("STT2", "${jsonResponse}")
                // Lấy giá trị từ các trường trong JSON
                val text = jsonResponse.getString("text")
                return text
                // Kiểm tra và xử lý giá trị response
            } else {
                println("Response failed with code: ${response.code}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "Error connected2"
    }
    // get sample
    override suspend fun updateMisPronunScreenData(difMode: Int): ResponseMisPronun? {
        // API Key và URL
        var result : ResponseMisPronun ?=null
        val apiKey = "rll5QsTiv83nti99BW6uCmvs9BDVxSB39SVFceYb"
        val apiMainPathSample = "https://a3hj0l2j2m.execute-api.eu-central-1.amazonaws.com/Prod"

        // Tạo HTTP Client
        val client = OkHttpClient()

        val getSampleJsonData = """
        {
            "category": "$difMode",
            "language": "en"
        }
    """.trimIndent()

        // Định nghĩa kiểu dữ liệu
        val mediaType = "application/json".toMediaType()

        val requestBody = getSampleJsonData.toRequestBody(mediaType)

        val newRequest = Request.Builder()
            .url("$apiMainPathSample/getSample")
            .post(requestBody)
            .addHeader("X-Api-Key", apiKey)
            .build()
        try {
            // Thực hiện yêu cầu
            val response: Response = client.newCall(newRequest).execute()
            Log.d("default","${response.isSuccessful}")
            if (response.isSuccessful) {
                val responseBody: String = response.body?.string() ?: ""
                val gson = Gson()
                val apiResponse = gson.fromJson(responseBody, ApiGetSample::class.java)

                try {
                    //println("JSON body: $responseBody")
//                    println("JSON body: ${apiResponse.real_transcript[0]}")
//                    println("JSON body: ${apiResponse.ipa_transcript}")
//                    println("JSON body: ${apiResponse.transcript_translation}")
                    result = ResponseMisPronun(apiResponse.real_transcript[0], apiResponse.ipa_transcript)
                    return result
                    //Cập nhật sau khi nhận phản hồi
                    /// UpdateGetSample(apiResponse)
                } catch (e: Exception) {
                    // Bắt lỗi nếu không thể phân tích chuỗi JSON
                    println("Error parsing JSON: ${e.message}")
                    //UpdateGetSampleError("Error parsing JSON")
                }
            } else {
                //Log.e("HTTP_ERROR", "Error code: ${response.code}")
                println("Error code: ${response.code}")
                //UpdateGetSampleError("Api error code")
            }
        } catch (e: Exception) {
            Log.e("HTTP_EXCEPTION", "Exception occurred: ${e.message}", e)
            e.printStackTrace()
            //UpdateGetSampleError("HTTP_EXCEPTION")
        }
        return result
    }

    override suspend fun postRequest(curTranscript: String, audio: String): ResponsePostRequest? {
        var result2 : ResponsePostRequest ?=null
        val apiKey = "rll5QsTiv83nti99BW6uCmvs9BDVxSB39SVFceYb"
        val apiMainPathSTS = "https://wrg7ayuv7i.execute-api.eu-central-1.amazonaws.com/Prod"

        // Tạo HTTP Client
        val client = OkHttpClient()
        val listArr = listOf<String>(curTranscript)
        val initJsonData = """
        {
            "title": "${listArr[0]}",
            "base64Audio": "$audio",
            "language": "en"
        }
    """.trimIndent()

        // Định nghĩa kiểu dữ liệu
        val mediaType = "application/json".toMediaType()

        val requestBody = initJsonData.toRequestBody(mediaType)

        val newRequest = Request.Builder()
            .url("$apiMainPathSTS/GetAccuracyFromRecordedAudio")
            .post(requestBody)
            .addHeader("X-Api-Key", apiKey)
            .build()
        try {
            // Thực hiện yêu cầu
            val response: Response = client.newCall(newRequest).execute()
            Log.d("post request","${response.isSuccessful}")
            //println("PRINT TEST")
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                Log.d("API","API true")
                val gson = Gson()
                val apiResponse = gson.fromJson(responseBody, ApiGetAccuracy::class.java)

                try {
                    result2 = ResponsePostRequest(
                        apiResponse.ipa_transcript,
                        apiResponse.pronunciation_accuracy,
                        apiResponse.real_transcripts,
                        apiResponse.real_transcripts_ipa,
                        apiResponse.is_letter_correct_all_words
                        )
                    Log.d("API","${result2}")
                    return result2
                    /// UpdateGetAccuracy(apiResponse)

                } catch (e: Exception) {
                    // Bắt lỗi nếu không thể phân tích chuỗi JSON
                    println("Error parsing JSON: ${e.message}")
                    //UpdateGetAccuracyError("Error parsing JSON")
                }
            } else {
                println("Error code: ${response.code}")
                //UpdateGetAccuracyError()
            }
        } catch (e: Exception) {
            Log.e("HTTP_EXCEPTION", "Exception occurred: ${e.message}", e)
            e.printStackTrace()
            //UpdateGetAccuracyError()
        }
        Log.d("API", "failed")
        return result2
    }

}


data class ApiGetSample(
    val real_transcript: List<String>,
    val ipa_transcript: String,
    val transcript_translation: String
)

data class ApiGetAccuracy(
    val real_transcript: String,
    val ipa_transcript: String,
    val pronunciation_accuracy: String,
    val real_transcripts: String,
    val matched_transcripts: String,
    val real_transcripts_ipa: String,
    val matched_transcripts_ipa: String,
    val pair_accuracy_category: String,
    val start_time: String,
    val end_time: String,
    val is_letter_correct_all_words: String
)
