package com.app.base.common.gallery

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import com.android.base.imageloader.ImageLoaderFactory
import com.android.base.imageloader.LoadListener
import com.android.base.imageloader.Source
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import kotlinx.android.synthetic.main.gallery_item_photo.view.*
import me.ztiany.widget.viewpager.BannerViewPagerAdapter
import uk.co.senab.photoview.PhotoViewAttacher
import java.lang.ref.WeakReference

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-18 17:42
 */
class GalleryPagerAdapter(
        private val thumbnailMap: Map<Uri, Uri>?
) : BannerViewPagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = createBannerPagerView(container, context)
        val uri = entities[position]
        bindViewItem(itemView, uri, position)
        container.addView(itemView)
        return itemView
    }

    private fun createBannerPagerView(container: ViewGroup, context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.gallery_item_photo, container, false)
    }

    private fun bindViewItem(itemView: View, uri: Uri, position: Int) {
        if (!URLUtil.isNetworkUrl(uri.toString()) || thumbnailMap.isNullOrEmpty()) {
            val galleryIv = itemView.galleryIv
            ImageLoaderFactory.getImageLoader().display(galleryIv, Source.create(uri))
        } else {
            itemView.galleryPb.visible()
            itemView.galleryIvThumbnail.visible()
            ImageLoaderFactory.getImageLoader().display(itemView.galleryIvThumbnail, Source.create(thumbnailMap[uri]))
            loadRaw(itemView, uri)
        }

        itemView.galleryIv.setOnPhotoTapListener(object : PhotoViewAttacher.OnPhotoTapListener {
            override fun onOutsidePhotoTap() = Unit
            override fun onPhotoTap(view: View?, x: Float, y: Float) {
                callPagerClicked(position, itemView.galleryIv)
            }
        })
    }

    private fun loadRaw(itemView: View, uri: Uri) {
        val weakIv = WeakReference(itemView.galleryIvThumbnail)
        val weakPb = WeakReference(itemView.galleryPb)
        ImageLoaderFactory.getImageLoader().display(itemView.galleryIv, uri.toString(), object : LoadListener<Drawable> {
            override fun onLoadSuccess(resource: Drawable?) {
                weakIv.get()?.gone()
                weakPb.get()?.gone()
            }
        })
    }

}