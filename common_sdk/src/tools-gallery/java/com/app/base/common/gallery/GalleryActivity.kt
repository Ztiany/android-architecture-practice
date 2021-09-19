package com.app.base.common.gallery

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.transition.Transition
import android.view.Menu
import android.view.View
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.android.base.app.activity.BaseActivity
import com.android.base.app.ui.viewBinding
import com.android.base.foundation.common.ActFragWrapper
import com.android.base.imageloader.ImageLoaderFactory
import com.android.base.imageloader.Source
import com.android.base.interfaces.TransitionListenerAdapter
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.views.*
import com.android.base.utils.common.toArrayList
import com.app.base.R
import com.app.base.databinding.GalleryActivityBinding
import kotlinx.parcelize.Parcelize

/**
 * 浏览图片。
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-23 10:50
 */
class GalleryActivity : BaseActivity() {

    @Parcelize
    data class GalleryInfo(
        val photos: ArrayList<Uri>,
        val thumbPhotos: ArrayList<Uri>?,
        val title: String,
        val position: Int,
        val useTransition: Boolean,
        val deletable: Boolean
    ) : Parcelable

    class Builder {

        private val photos = arrayListOf<Uri>()
        private var thumbPhotos: ArrayList<Uri>? = null
        private var title: String = ""
        private var position = 0
        private var useTransition = true
        private var transView: View? = null
        private var deletable: Boolean = false

        /**
         * 如果在menu中有删除源图片操作，则不推荐使用Transition。
         *
         * @param useTransition 是否用shareElement方式打开Activity
         */
        fun setUseTransition(useTransition: Boolean): Builder {
            this.useTransition = useTransition
            return this
        }

        fun setPhotos(photos: List<Uri>, thumbPhotos: List<Uri>? = null): Builder {
            this.photos.clear()
            this.photos.addAll(photos)
            this.thumbPhotos = thumbPhotos?.toArrayList()
            if (!thumbPhotos.isNullOrEmpty() && thumbPhotos.size != photos.size) {
                throw IllegalArgumentException("photos and thumbPhotos must have the same size.")
            }
            return this
        }

        fun setPosition(position: Int): Builder {
            this.position = position
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setTransitionView(transView: View): Builder {
            this.transView = transView
            return this
        }

        fun setDeletable(deletable: Boolean): Builder {
            this.deletable = deletable
            return this
        }

        fun start(fragment: Fragment) {
            start(ActFragWrapper.create(fragment))
        }

        fun start(activity: Activity) {
            start(ActFragWrapper.create(activity))
        }

        fun start(actFragWrapper: ActFragWrapper) {
            val intent = Intent(actFragWrapper.context, GalleryActivity::class.java).apply {
                putExtra(
                    KEY_FOR_BUILDER,
                    GalleryInfo(photos, thumbPhotos, title, position, useTransition, deletable)
                )
            }

            val share = transView

            if (useTransition && share != null && useTransition) {
                if (AndroidVersion.atLeast(21)) {
                    actFragWrapper.startActivityForResult(
                        intent,
                        REQUEST_CODE,
                        ActivityOptions.makeSceneTransitionAnimation(
                            actFragWrapper.context as Activity,
                            share,
                            share.transitionName
                        ).toBundle()
                    )
                } else {//小于5.0，使用makeScaleUpAnimation
                    val options = ActivityOptionsCompat.makeScaleUpAnimation(
                        share,
                        share.width / 2,
                        share.height / 2,
                        0,
                        0
                    )
                    actFragWrapper.startActivityForResult(intent, REQUEST_CODE, options.toBundle())
                }
            } else {
                actFragWrapper.startActivityForResult(intent, REQUEST_CODE, null)
            }
        }
    }

    private val layout by viewBinding(GalleryActivityBinding::bind)

    private lateinit var galleryInfo: GalleryInfo
    private var curPosition = 0
    private lateinit var initializedPositionPhoto: Uri
    private var transitionPhotoDeleted = false

    private val deletedPhotos by lazy { arrayListOf<Uri>() }

    private var background: ColorDrawable = ColorDrawable(Color.BLACK)

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)

        galleryInfo = intent.getParcelableExtra(KEY_FOR_BUILDER) ?: throw NullPointerException()
        curPosition = galleryInfo.position
        initializedPositionPhoto = galleryInfo.photos[curPosition]

        SystemBarCompat.setTranslucentSystemUi(this, true, false)
        if (AndroidVersion.atLeast(21)) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }
    }

    override fun provideLayout() = R.layout.gallery_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        val transUri = galleryInfo.thumbPhotos?.get(curPosition) ?: galleryInfo.photos[curPosition]
        ImageLoaderFactory.getImageLoader()
            .display(layout.commonGalleryIvTrans, Source.create(transUri))

        if (galleryInfo.useTransition && AndroidVersion.atLeast(21)) {
            window.enterTransition.addListener(object : TransitionListenerAdapter {
                override fun onTransitionEnd(transition: Transition?) {
                    initView()
                }
            })
        } else {
            initView()
        }
    }

    private fun initView() {
        layout.commonGalleryFlBackground.background = background
        setupViewPager()
        setupToolbar()
        setupPullback()
        showOrHideToolBar(true)
        window.decorView.post {
            layout.commonGalleryIvTrans.invisible()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(layout.commonGalleryToolbar)
        val supportActionBar = supportActionBar

        if (supportActionBar != null) {
            supportActionBar.title = galleryInfo.title
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            layout.commonGalleryToolbar.contentInsetStartWithNavigation = 0
            layout.commonGalleryToolbar.setNavigationOnClickListener { finishSelf(false) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null && galleryInfo.deletable) {
            menu.add(R.string.delete)
                .setIcon(R.drawable.icon_delete)
                .alwaysShow()
                .onMenuItemClick {
                    deleteCurrentImage()
                }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun deleteCurrentImage() {
        if (curPosition in 0 until galleryInfo.photos.size) {
            deletedPhotos.add(galleryInfo.photos.removeAt(curPosition))
            transitionPhotoDeleted = deletedPhotos.contains(initializedPositionPhoto)
            if (galleryInfo.photos.isEmpty()) {
                finishSelf(false)
            } else {
                setupViewPager()
            }
        }
    }

    private fun setupViewPager() {
        with(layout.commonGalleryVp) {
            val previous = curPosition
            setEnableLooper(false)
            setOnBannerPositionChangedListener { position ->
                curPosition = position
            }
            setOnPageClickListener { _, _ ->
                finishSelf(false)
            }
            setImages(galleryInfo.photos, GalleryPagerAdapter(mapThumbnail()))
            setCurrentPosition(previous)
        }
    }

    private fun mapThumbnail(): Map<Uri, Uri>? {
        val thumbPhotos = galleryInfo.thumbPhotos
        if (thumbPhotos.isNullOrEmpty()) {
            return null
        }
        val map = mutableMapOf<Uri, Uri>()
        galleryInfo.photos.forEachIndexed { index, item ->
            map[item] = thumbPhotos[index]
        }
        return map
    }

    private fun setupPullback() {
        layout.commonGalleryViewPullback.setCallback(object : PullBackLayout.Callback {
            override fun onPullStart() {
                showOrHideToolBar(false)
            }

            override fun onPull(progress: Float) {
                val value = 1F.coerceAtMost(progress * 3f)
                background.alpha = (0xFF * (1f - value)).toInt()
            }

            override fun onPullCancel() {
                showOrHideToolBar(true)
            }

            override fun onPullComplete() {
                finishSelf(true)
            }
        })
    }

    private fun showOrHideToolBar(show: Boolean) {
        layout.commonGalleryToolbar.animate().alpha(if (show) 1.0f else 0.0f)
    }

    override fun onBackPressed() {
        finishSelf(false)
    }

    private fun finishSelf(scale: Boolean) {
        if (galleryInfo.deletable && deletedPhotos.isNotEmpty()) {
            setResult(Activity.RESULT_OK, Intent().apply {
                putParcelableArrayListExtra(KEY_FOR_RESULT, deletedPhotos)
            })
        }

        when {
            !transitionPhotoDeleted && galleryInfo.position == curPosition -> {
                layout.commonGalleryVp.invisible()
                layout.commonGalleryIvTrans.visible()
                supportFinishAfterTransition()
            }
            scale -> this.finishWithAnimation(0, 0)
            else -> this.finishWithAnimation(0, R.anim.gallery_scale_out)
        }
    }

    companion object {

        private const val KEY_FOR_BUILDER = "key_for_builder"
        private const val KEY_FOR_RESULT = "deleted_photo_for_result"

        private const val REQUEST_CODE = 1096

        fun newBuilder(): Builder {
            return Builder()
        }

        fun handleResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            deletedPhotos: (deleted: List<Uri>) -> Unit
        ) {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                val listExtra = data.getParcelableArrayListExtra<Uri>(KEY_FOR_RESULT)
                if (!listExtra.isNullOrEmpty()) {
                    deletedPhotos(listExtra)
                }
            }
        }
    }

}