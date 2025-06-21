package com.example.mediauploader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import java.io.IOException

class MediaViewActivity : AppCompatActivity() {

    private lateinit var adapter: MediaAdapter
    private val serverUrl = "Your Flask IP" //
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_view)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val deleteBtn = findViewById<Button>(R.id.deleteButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchMediaList { mediaList ->
            runOnUiThread {
                if (mediaList != null) {
                    adapter = MediaAdapter(mediaList, serverUrl, true)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this, "Failed to fetch media", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteBtn.setOnClickListener {
            val selected = adapter.getSelectedMedia()
            if (selected.isEmpty()) {
                Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            deleteMediaFromServer(selected) {
                progressBar.visibility = View.GONE
                fetchMediaList { updatedList ->
                    runOnUiThread {
                        adapter = MediaAdapter(updatedList ?: listOf(), serverUrl, true)
                        recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    private fun fetchMediaList(callback: (List<String>?) -> Unit) {
        val request = Request.Builder()
            .url("$serverUrl/media")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FetchMedia", "Failed", e)
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val mediaList = mutableListOf<String>()
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    mediaList.add(jsonArray.getString(i))
                }
                callback(mediaList)
            }
        })
    }

    private fun deleteMediaFromServer(fileNames: List<String>, onDone: () -> Unit) {
        val jsonBody = """
            {
                "filenames": ${fileNames.joinToString(prefix = "[\"", separator = "\",\"", postfix = "\"]")}
            }
        """.trimIndent()

        val body = RequestBody.create("application/json".toMedeiaTypeOrNull(), jsonBody)
        val request = Request.Builder()
            .url("$serverUrl/delete")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MediaViewActivity, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    onDone()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MediaViewActivity, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MediaViewActivity, "Delete failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    onDone()
                }
            }
        })
    }
}
