package me.ztiany.wan.sample.selector

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.utils.setRequireOriginal
import com.bumptech.glide.Glide
import me.ztiany.wan.sample.R
import timber.log.Timber
import java.io.File
import java.io.InputStream

const val KEY = "Results"

class MediaResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity_meida_result)

        val list = intent.getParcelableArrayListExtra(KEY) ?: emptyList<MediaItem>()

        Timber.d("list: $list")

        val rv = findViewById<RecyclerView>(R.id.rv_result)
        PagerSnapHelper().attachToRecyclerView(rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val imageView = AppCompatImageView(this@MediaResultActivity)
                imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.setOnClickListener {
                    (it.tag as MediaItem).let(::showMediaInfoChecked)
                }
                return object : RecyclerView.ViewHolder(imageView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.tag = list[position]
                Glide.with(holder.itemView).load(list[position].uri).into(holder.itemView as ImageView)
            }

            override fun getItemCount(): Int {
                return list.size
            }
        }
    }

    private fun showMediaInfoChecked(item: MediaItem) {
        askMediaLocationPermission(
            onGranted = {
                item.rawUri.setRequireOriginal(this)
                extractLocation(item)
            },
            onDenied = {
                Timber.e("Permission denied")
            })
    }

    private fun extractLocation(item: MediaItem) {
        if (item.rawUri.toString().startsWith("content://")) {
            val stream = contentResolver.openInputStream(item.rawUri)
            if (stream == null) {
                Timber.d("showMediaInfo, protocol is content, but openInputStream failed.")
                Toast.makeText(this, "showMediaInfo, protocol is content, but openInputStream failed.", Toast.LENGTH_LONG).show()
            } else {
                Timber.d("showMediaInfo, protocol is content, but openInputStream succeeded.")
                showMediaInfo(item.rawUri, stream)
            }
        } else {
            val path = item.uri.path
            if (path.isNullOrEmpty()) {
                Timber.d("showMediaInfo, protocol is file, but path is null.")
                Toast.makeText(this, "showMediaInfo, protocol is file, but path is null.", Toast.LENGTH_LONG).show()
            } else {
                val file = File(path)
                Timber.d("showMediaInfo, protocol is file, file exist = ${file.exists()}")
                showMediaInfo(item.rawUri, file.inputStream())
            }
        }
    }

    private fun showMediaInfo(uri: Uri, stream: InputStream?) {
        Timber.d("showMediaInfo() called with: uri = $uri, stream = $stream")
        stream?.use {
            ExifInterface(stream).run {
                val latLong = latLong ?: doubleArrayOf(0.0, 0.0)
                Timber.e("%s latLong = %s", uri.toString(), latLong.contentToString())
                Toast.makeText(this@MediaResultActivity, "latLong = ${latLong.contentToString()}", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(this, "showMediaInfo, stream is null", Toast.LENGTH_LONG).show()
    }

}