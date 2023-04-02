package com.example.myscreenshotapplication

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Screenshot : AppCompatActivity() {
    var take: Button? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val permissionstorage = arrayOf<String>(
        WRITE_EXTERNAL_STORAGE,
        READ_EXTERNAL_STORAGE,
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkpermissions(this); // check o
        val take = findViewById<Button>(R.id.take)
        take.setOnClickListener {
            val file = takeScreenshot(getWindow().getDecorView().getRootView())
        }
    }

    protected fun takeScreenshot(view: View): File? {
        val date = Date()
        try {
            val dirpath: String
            // Initialising the directory of storage
            dirpath =
                this@Screenshot.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val file = File(dirpath)
            if (!file.exists()) {
                val mkdir = file.mkdir()
            }
            // File name : keeping file name unique using data time.
            val path = dirpath + "/" + date.getTime() + ".jpeg"
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            val imageurl = File(path)
            val outputStream = FileOutputStream(imageurl)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
            Log.d("TAG", "takeScreenshot Path: $imageurl")
            val size = imageurl.length() / (1024 * 1024)
            Log.d("TAG", "takeScreenshot Size: $size")
            Toast.makeText(this@Screenshot, "" + imageurl, Toast.LENGTH_LONG).show()
            return imageurl
        } catch (io: FileNotFoundException) {
            io.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun checkpermissions(activity: Activity?) {
        val permissions = ActivityCompat.checkSelfPermission(
            activity!!,
            WRITE_EXTERNAL_STORAGE,
        )
        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE)
        }
    }
}