package me.ztiany.wan.sample.selector

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.base.delegate.simpl.DelegateActivity
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.SelectorConfigurer
import com.android.sdk.mediaselector.buildMediaSelector
import me.ztiany.wan.sample.R
import timber.log.Timber
import timber.log.Timber.DebugTree

class MediaSelectorActivity : DelegateActivity() {

    private val mediaSelector = buildMediaSelector {
        withPostProcessor(compressImage())
        onResults { handleSelectedItems(it) }
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

    private fun handleSelectedItems(results: List<MediaItem>) {
        results.forEach {
            Timber.e("item :$it")
        }

        if (takingByMediaStore) {
            selectedItems = results
            takingByMediaStore = false
        }

        if (takingFile) {
            takingFile = false
            showFiles(results)
        } else {
            showMedias(results)
        }
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
        mediaSelector.imageCapturer().start()
    }

    fun captureOneVideo(view: View) {
        mediaSelector.videoCapturer().start()
    }

    fun captureOnePhotoAndCrop(view: View) {
        mediaSelector.imageCapturer().crop().start()
    }

    fun selectOnePhoto(view: View) {
        mediaSelector.systemImagePicker().start()
    }

    fun selectOnePhotoAndCrop(view: View) {
        mediaSelector.systemImagePicker().crop().start()
    }

    fun selectPhotos(view: View) {
        mediaSelector.systemImagePicker().count(4).start()
    }

    fun selectPhotosAndCrop(view: View) {
        mediaSelector.systemImagePicker().count(4).crop().start()
    }

    fun selectVideos(view: View) {
        mediaSelector.systemVideoPicker().count(4).start()
    }

    fun selectPhotoAndVideo(view: View) {
        mediaSelector.systemMediaPicker().count(4).crop().start()
    }

    fun selectPhotosByGetContent(view: View) {
        mediaSelector.imageContentGetter().multiple().crop().start()
    }

    fun selectVideosByGetContent(view: View) {
        mediaSelector.videoContentGetter().multiple().start()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Files by Intent or SAF
    ///////////////////////////////////////////////////////////////////////////
    fun selectFile(view: View) {
        takingFile = true
        mediaSelector.systemFilePicker().start()
    }

    fun selectFiles(view: View) {
        takingFile = true
        mediaSelector.systemFilePicker().multiple().start()
    }

    fun selectFilesByGetContent(view: View) {
        takingFile = true
        mediaSelector.contentGetter().multiple().start()
    }

    ///////////////////////////////////////////////////////////////////////////
    // MediaStore
    ///////////////////////////////////////////////////////////////////////////
    fun selectOnePhotoByMediaStore(view: View) {
        mediaSelector.imageSelector().start()
    }

    fun selectOnePhotoWithCameraByMediaStore(view: View) {
        mediaSelector.imageSelector().enableCamera().start()
    }

    fun selectOnePhotoWithCameraAndCropByMediaStore(view: View) {
        mediaSelector.imageSelector().enableCamera().crop().includeGif().start()
    }

    fun selectMultiPhotoByMediaStore(view: View) {
        takingByMediaStore = true
        mediaSelector.imageSelector().count(9).includeGif().selectedData(selectedItems.map { it.id }).start()
    }

    fun selectOneVideoByMediaStore(view: View) {
        mediaSelector.videoSelector().start()
    }

}