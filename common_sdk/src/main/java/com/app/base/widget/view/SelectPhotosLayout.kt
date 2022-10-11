package com.app.base.widget.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.image.ImageLoaderFactory
import com.android.base.image.Source
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.invisible
import com.android.base.utils.android.views.use
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.app.base.databinding.WidgetSelectPhotoItemPhotoBinding
import com.android.base.ui.recyclerview.MarginDecoration
import kotlin.math.max

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-03 13:32
 */
class SelectPhotosLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var selectPhotosLayoutCallback: ((residueSize: Int) -> Unit)? = null

    var onPhotoClickCallback: ((itemView: View, photos: List<Uri>, position: Int) -> Unit)? = null

    var onPhotoDeletedCallback: ((photos: List<Uri>) -> Unit)? = null

    private val addImageAdapter: AddImageAdapter

    private var maxImageSize = 1

    init {

        context.obtainStyledAttributes(attrs, R.styleable.SelectPhotosLayout).use {
            maxImageSize = it.getInt(R.styleable.SelectPhotosLayout_spl_max_image_size, 1)
        }

        require(maxImageSize >= 1) { "imageSize must > 1" }

        addImageAdapter = AddImageAdapter(context, maxImageSize,
                onAddImageListener = {
                    selectPhotosLayoutCallback?.invoke(it)
                },
                onClickImageListener = { view, photos, index ->
                    onPhotoClickCallback?.invoke(view, photos, index)
                },
                onPhotoDeletedCallback = {
                    onPhotoDeletedCallback?.invoke(it)
                }
        )

        setupRecyclerView()

        //adjust padding
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    private fun setupRecyclerView() {
        layoutManager = GridLayoutManager(context, 3)
        addItemDecoration(MarginDecoration(dip(5), dip(5), dip(5), dip(5)))
        adapter = addImageAdapter
        isNestedScrollingEnabled = false
    }

    fun addPhotos(photos: List<Uri>) {
        addImageAdapter.addImages(photos)
    }

    fun setPhotos(photos: List<Uri>) {
        addImageAdapter.setImages(photos)
    }

    fun selectedPhotos(): List<Uri> = addImageAdapter.getImages()

    fun hasPhotos() = !addImageAdapter.isEmpty()

    fun removeImage(image: Uri, notify: Boolean = false) {
        addImageAdapter.removeImage(image)
        if (notify) {
            onPhotoDeletedCallback?.invoke(listOf(image))
        }
    }

    fun removeImages(images: List<Uri>, notify: Boolean = false) {
        addImageAdapter.removeImages(images)
        if (notify) {
            onPhotoDeletedCallback?.invoke(images)
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(max(0, left - dip(5)), top, max(0, right - dip(5)), bottom)
    }

}

private class AddImageAdapter(
        private val context: Context,
        private val maxImageSize: Int,
        onAddImageListener: (Int) -> Unit,
        onClickImageListener: (view: View, photos: List<Uri>, position: Int) -> Unit,
        var onPhotoDeletedCallback: ((photos: List<Uri>) -> Unit)? = null
) : RecyclerView.Adapter<BindingViewHolder<WidgetSelectPhotoItemPhotoBinding>>() {

    companion object {
        private val ADD = Uri.EMPTY
    }

    private val imageLoader = ImageLoaderFactory.getImageLoader()

    private val _onAddImageListener = View.OnClickListener {
        if (dataList.contains(ADD)) {
            onAddImageListener(maxImageSize - dataList.size + 1)
        } else {
            onAddImageListener(maxImageSize - dataList.size)
        }
    }

    private val _onDeleteImageListener = View.OnClickListener {
        val index = it.tag as Int
        val removed = dataList.removeAt(index)
        if (dataList.contains(ADD)) {
            notifyItemRemoved(index)
        } else {
            dataList.add(ADD)
            notifyDataSetChanged()
        }
        onPhotoDeletedCallback?.invoke(listOf(removed))
    }

    private val _onClickImageListener = View.OnClickListener {
        onClickImageListener(it, dataList.filter { photo -> photo != ADD }, it.tag as Int)
    }

    private val dataList = mutableListOf(ADD)

    fun isEmpty(): Boolean {
        if (dataList.isEmpty()) {
            return true
        }
        if (dataList.contains(ADD)) {
            return dataList.size == 1
        }
        return false
    }

    fun getImages(): List<Uri> {
        val list = ArrayList<Uri>(dataList)
        list.remove(ADD)
        return list
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<WidgetSelectPhotoItemPhotoBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return BindingViewHolder(WidgetSelectPhotoItemPhotoBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: BindingViewHolder<WidgetSelectPhotoItemPhotoBinding>, position: Int) {
        val item = dataList[position]

        if (item == ADD) {
            viewHolder.vb.widgetSelectingIvPhoto.setImageResource(R.drawable.img_add_photo)
            viewHolder.vb.widgetSelectingIvPhoto.setOnClickListener(_onAddImageListener)
            viewHolder.vb.widgetSelectingIvDelete.invisible()
        } else {
            imageLoader.display(viewHolder.vb.widgetSelectingIvPhoto, Source.create(item))
            viewHolder.vb.widgetSelectingIvPhoto.tag = viewHolder.adapterPosition
            viewHolder.vb.widgetSelectingIvPhoto.setOnClickListener(_onClickImageListener)
            viewHolder.vb.widgetSelectingIvDelete.setOnClickListener(_onDeleteImageListener)
            viewHolder.vb.widgetSelectingIvDelete.tag = viewHolder.adapterPosition
            viewHolder.vb.widgetSelectingIvDelete.visible()
        }

    }

    fun addImages(pictures: List<Uri>) {
        dataList.remove(ADD)
        dataList.addAll(pictures)
        if (dataList.size < maxImageSize && !dataList.contains(ADD)) {
            dataList.add(ADD)
        }
        notifyDataSetChanged()
    }

    fun removeImage(image: Uri) {
        if (dataList.remove(image)) {
            if (!dataList.contains(ADD)) {
                dataList.add(ADD)
            }
            notifyDataSetChanged()
        }
    }

    fun removeImages(images: List<Uri>) {
        if (dataList.removeAll(images)) {
            if (!dataList.contains(ADD)) {
                dataList.add(ADD)
            }
            notifyDataSetChanged()
        }
    }

    fun setImages(photos: List<Uri>) {
        require(photos.size <= maxImageSize) {
            "size of photos is bigger than maxImageSize"
        }
        dataList.clear()
        dataList.addAll(photos)
        if (dataList.size < maxImageSize) {
            dataList.add(ADD)
        }
        notifyDataSetChanged()
    }

}