package com.app.base.common.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.base.ui.banner.BannerViewPagerAdapter
import com.app.base.databinding.GalleryItemPhotoBinding

/**
 *@author Ztiany
 */
class GalleryPagerAdapter(
    private val thumbnailMap: Map<Uri, Uri>?,
) : BannerViewPagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = createBannerPagerView(container, context)
        val uri = entities[position]
        bindViewItem(binding, uri, position)
        container.addView(binding.root)
        return binding
    }

    private fun createBannerPagerView(container: ViewGroup, context: Context): GalleryItemPhotoBinding {
        return GalleryItemPhotoBinding.inflate(LayoutInflater.from(context), container, false)
    }

    private fun bindViewItem(binding: GalleryItemPhotoBinding, uri: Uri, position: Int) {
        /*if (!URLUtil.isNetworkUrl(uri.toString()) || thumbnailMap.isNullOrEmpty()) {
            val galleryIv = binding.galleryIv
            ImageLoaderFactory.getImageLoader().display(galleryIv, Source.create(uri))
        } else {
            binding.galleryPb.beVisible()
            binding.galleryIvThumbnail.beVisible()
            ImageLoaderFactory.getImageLoader().display(binding.galleryIvThumbnail, Source.create(thumbnailMap[uri]))
            loadRaw(binding, uri)
        }

        binding.galleryIv.setOnPhotoTapListener(object : PhotoViewAttacher.OnPhotoTapListener {
            override fun onOutsidePhotoTap() = Unit
            override fun onPhotoTap(view: View?, x: Float, y: Float) {
                callPagerClicked(position, binding.galleryIv)
            }
        })*/
    }

    private fun loadRaw(binding: GalleryItemPhotoBinding, uri: Uri) {
        /*val weakIv = WeakReference(binding.galleryIvThumbnail)
        val weakPb = WeakReference(binding.galleryPb)
        ImageLoaderFactory.getImageLoader().display(binding.galleryIv, uri.toString(), object : LoadListener<Drawable> {
            override fun onLoadSuccess(resource: Drawable?) {
                weakIv.get()?.beGone()
                weakPb.get()?.beGone()
            }
        })*/
    }

}