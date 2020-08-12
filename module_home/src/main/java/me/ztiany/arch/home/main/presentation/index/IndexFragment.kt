package me.ztiany.arch.home.main.presentation.index

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.android.base.app.fragment.BaseFragment
import com.android.base.imageloader.ImageLoaderFactory
import com.android.base.imageloader.Source
import com.android.base.permission.AutoPermissionRequester
import com.android.base.permission.Permission
import com.android.base.utils.android.views.onDebouncedClick
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.custom.newMediaSelector
import com.android.sdk.mediaselector.system.newSystemMediaSelector
import com.app.base.AppContext
import com.app.base.router.RouterPath
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.index_fragment.*
import me.ztiany.arch.home.main.MainFragment
import me.ztiany.architecture.home.R
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
@AndroidEntryPoint
class IndexFragment : BaseFragment(), MainFragment.MainFragmentChild {

    private val viewModel: IndexViewModule by viewModels()

    private val mediaSelector by lazy {
        newMediaSelector(this, object : ResultListener {
            override fun onTakeSuccess(result: List<Uri>) {
                Timber.d("----------------------------------------picture $result")
                ImageLoaderFactory.getImageLoader().display(image, Source.createWithUri(result[0]))
            }
        })
    }

    private val systemMediaSelector by lazy {
        newSystemMediaSelector(this, object : ResultListener {
            override fun onTakeSuccess(result: List<Uri>) {
                Timber.d("----------------------------------------picture $result")
                ImageLoaderFactory.getImageLoader().display(image, Source.createWithUri(result[0]))
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        viewModel.demo.observe(this) {
            showMessage(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AutoPermissionRequester.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request()
    }

    override fun provideLayout() = R.layout.index_fragment

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)

        ImageLoaderFactory.getImageLoader().display(image, Source.create(R.drawable.icon_back))

        indexBtnOpenAccount.onDebouncedClick {
            AppContext.appRouter().build(RouterPath.Account.PATH).navigation()
        }

        indexBtnCustomSelectPicture.onDebouncedClick {
            AutoPermissionRequester.with(this)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                    .onGranted {
                        mediaSelector.takeMedia().crop().needCamera().start()
                    }
                    .request()
        }

        indexBtnSystemSelectPicture.setOnClickListener {
            AutoPermissionRequester.with(this)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                    .onGranted {
                        systemMediaSelector
                                .takePhotoFromSystem()
                                .setNeedCrop()
                                .start()
                    }
                    .request()
        }

    }

}