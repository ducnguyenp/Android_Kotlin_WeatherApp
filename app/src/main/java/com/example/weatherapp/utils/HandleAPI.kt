package com.example.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.weatherapp.DataList
import com.example.weatherapp.R
import com.example.weatherapp.ResponseData
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class HandleAPI(var context: Context, val username: String, val password: String) :
    AsyncTask<Any, Void, String>() {
    private lateinit var customProgressDialog: Dialog

    override fun onPreExecute() {
        super.onPreExecute()
        showProgressDialog()
    }

    override fun doInBackground(vararg params: Any): String {
        var result: String
        var connection: HttpURLConnection? = null
        try {
            val url = URL("http://www.mocky.io/v2/5e3826143100006a00d37ffa")
            connection = url.openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.doInput = true
            connection.instanceFollowRedirects = false
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.useCaches = false

            val wr = DataOutputStream(connection.outputStream)
            val jsonRequest = JSONObject()
            jsonRequest.put("username", username) // Request Parameter 1
            jsonRequest.put("password", password) // Request Parameter 2
            wr.writeBytes(jsonRequest.toString())
            wr.flush()
            wr.close()

            val httpResult: Int = connection.responseCode

            if (httpResult == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val sb = StringBuilder()
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line + "\n")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                result = sb.toString()
            } else {
                result = connection.responseMessage
            }

        } catch (e: SocketTimeoutException) {
            result = "Connection Timeout"
        } catch (e: Exception) {
            result = "Error : " + e.message
        } finally {
            connection?.disconnect()
        }
        return result
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        cancelProgressDialog()

        val responseData = Gson().fromJson(result, ResponseData::class.java)
        Log.i("JSON Response Result", responseData.toString())
        Log.i("Data", responseData.message)
        Log.i("Data", responseData.user_id)
        Log.i("Data", responseData.name)
        Log.i("Data", responseData.email)
        Log.i("Data", responseData.mobile)

        for (item in 0 until responseData.data_list.size) {
            val dataItemObject: DataList = responseData.data_list[item]
            Log.i("Data", dataItemObject.id.toString())
            Log.i("Data", dataItemObject.value)
        }

        Toast.makeText(context, "Whatever", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(context)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

    private fun cancelProgressDialog() {
        customProgressDialog.dismiss()
    }
}