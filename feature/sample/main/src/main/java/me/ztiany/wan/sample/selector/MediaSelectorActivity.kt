package me.ztiany.wan.sample.selector

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.base.delegate.simpl.DelegateActivity
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.SelectorConfigurer
import com.android.sdk.mediaselector.newMediaSelector
import me.ztiany.wan.sample.R
import timber.log.Timber
import timber.log.Timber.DebugTree

class MediaSelectorActivity : DelegateActivity() {

    private val mediaSelector = newMediaSelector { result ->
        result.forEach {
            Timber.e("item :$it")
        }

        if (takingByMediaStore) {
            selectedItems = result
            takingByMediaStore = false
        }

        if (takingFile) {
            takingFile = false
            showFiles(result)
        } else {
            showMedias(result)
        }
    }

    private var takingFile = false
    private var takingByMediaStore = false

    private lateinit var fileTextView: TextView

    private var selectedItems = emptyList<MediaItem>()

    override fun initialize(savedInstanceState: Bundle?) {
        Timber.plant(DebugTree())

        SelectorConfigurer.setUp {
            setCropPrimaryColorAttr(androidx.appcompat.R.attr.colorAccent)
        }
    }

    override fun provideLayout() = R.layout.sample_activity_media_selector

    override fun setUpLayout(savedInstanceState: Bundle?) {
        fileTextView = findViewById(R.id.selected_files)
    }

    private fun showMedias(results: List<MediaItem>) {
        val intent = Intent(this, MediaResultActivity::class.java)
        intent.putParcelableArrayListExtra(KEY, ArrayList(results))
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun showFiles(results: List<MediaItem>) {
        val files = results.joinToString(separator = "\n\n   ") {
            it.toString()
        }
        fileTextView.text = "Selected Files:\n\n   $files"
    }

    ///////////////////////////////////////////////////////////////////////////
    // Photos by Intent or SAF
    ///////////////////////////////////////////////////////////////////////////
    fun captureOnePhoto(view: View) {
        mediaSelector.captureImage().start()
    }

    fun captureOneVideo(view: View) {
        mediaSelector.captureVideo().start()
    }

    fun captureOnePhotoAndCrop(view: View) {
        mediaSelector.captureImage().crop().start()
    }

    fun selectOnePhoto(view: View) {
        mediaSelector.pickImage().start()
    }

    fun selectOnePhotoAndCrop(view: View) {
        mediaSelector.pickImage().crop().start()
    }

    fun selectPhotos(view: View) {
        mediaSelector.pickImage().count(4).start()
    }

    fun selectPhotosAndCrop(view: View) {
        mediaSelector.pickImage().count(4).crop().start()
    }

    fun selectVideos(view: View) {
        mediaSelector.pickVideo().count(4).start()
    }

    fun selectPhotoAndVideo(view: View) {
        mediaSelector.pickImageAndVideo().count(4).crop().start()
    }

    fun selectPhotosByGetContent(view: View) {
        mediaSelector.getImageContent().multiple().crop().start()
    }

    fun selectVideosByGetContent(view: View) {
        mediaSelector.getVideoContent().multiple().start()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Files by Intent or SAF
    ///////////////////////////////////////////////////////////////////////////
    fun selectFile(view: View) {
        takingFile = true
        mediaSelector.pickFile().start()
    }

    fun selectFiles(view: View) {
        takingFile = true
        mediaSelector.pickFile().multiple().start()
    }

    fun selectFilesByGetContent(view: View) {
        takingFile = true
        mediaSelector.getContent().multiple().start()
    }

    ///////////////////////////////////////////////////////////////////////////
    // MediaStore
    ///////////////////////////////////////////////////////////////////////////
    fun selectOnePhotoByMediaStore(view: View) {
        mediaSelector.selectImage().start()
    }

    fun selectOnePhotoWithCameraByMediaStore(view: View) {
        mediaSelector.selectImage().enableCamera().start()
    }

    fun selectOnePhotoWithCameraAndCropByMediaStore(view: View) {
        mediaSelector.selectImage().enableCamera().crop().includeGif().start()
    }

    fun selectMultiPhotoByMediaStore(view: View) {
        takingByMediaStore = true
        mediaSelector.selectImage().count(9).includeGif().selectedData(selectedItems.map { it.id }).start()
    }

    fun selectOneVideoByMediaStore(view: View) {
        mediaSelector.selectVideo().start()
    }

}