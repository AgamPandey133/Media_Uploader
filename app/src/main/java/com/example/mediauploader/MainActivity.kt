package com.example.mediauploader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PICK_MEDIA_REQUEST = 1
    private var selectedUris: List<Uri> = listOf()
    private val client = OkHttpClient()
    private val serverUrl = "Your Flask IP" //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectBtn = findViewById<Button>(R.id.selectButton)
        val uploadBtn = findViewById<Button>(R.id.uploadButton)
        val viewBtn = findViewById<Button>(R.id.viewMediaButton)

        selectBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), PICK_MEDIA_REQUEST)
        }

        uploadBtn.setOnClickListener {
            uploadMedia()
        }

        viewBtn.setOnClickListener {
            startActivity(Intent(this, MediaViewActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            val uris = mutableListOf<Uri>()
            data?.let {
                if (it.clipData != null) {
                    for (i in 0 until it.clipData!!.itemCount) {
                        uris.add(it.clipData!!.getItemAt(i).uri)
                    }
                } else {
                    it.data?.let { uri -> uris.add(uri) }
                }
            }
            selectedUris = uris
        }
    }

    private fun uploadMedia() {
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (uri in selectedUris) {
            val fileName = getFileName(uri)
            val inputStream = contentResolver.openInputStream(uri) ?: continue
            val bytes = inputStream.readBytes()
            multipartBodyBuilder.addFormDataPart(
                "media", fileName,
                RequestBody.create("application/octet-stream".toMediaTypeOrNull(), bytes)
            )
        }

        val request = Request.Builder()
            .url("$serverUrl/upload")
            .post(multipartBodyBuilder.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("Upload", "Failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Media uploaded successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getFileName(uri: Uri): String {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0 && it.moveToFirst()) {
                    name = it.getString(nameIndex)
                }
            }
        }

        // Fallback to lastPathSegment if DISPLAY_NAME is unavailable
        if (name == null) {
            name = uri.lastPathSegment?.substringAfterLast('/')
        }

        val viewBtn = findViewById<Button>(R.id.viewMediaButton)
        viewBtn.setOnClickListener {
            startActivity(Intent(this, MediaViewActivity::class.java))
        }

        return name ?: "file_${UUID.randomUUID()}"
    }

}
