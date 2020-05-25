package me.ztiany.arch.home.main.presentation.index

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.permission.AutoPermissionRequester
import com.android.base.permission.Permission
import com.android.base.utils.android.views.onDebouncedClick
import com.android.sdk.mediaselector.MediaSelector
import com.app.base.AppContext
import com.app.base.app.InjectorBaseFragment
import com.app.base.router.RouterPath
import kotlinx.android.synthetic.main.index_fragment.*
import me.ztiany.arch.home.main.MainFragment
import me.ztiany.architecture.home.R
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
class IndexFragment : InjectorBaseFragment(), MainFragment.MainFragmentChild {

    private val viewModel: IndexViewModule by viewModels { viewModelFactory }

    private val mediaSelector by lazy {
        MediaSelector(this, object : MediaSelector.Callback {
            override fun onTakeMultiPictureSuccess(pictures: MutableList<String>) {
                Timber.d("pictures $pictures")
            }

            override fun onTakePictureSuccess(picture: String) {
                Timber.d("picture $picture")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        //no op
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
        indexBtnOpenAccount.onDebouncedClick {
            AppContext.appRouter().build(RouterPath.Account.PATH).navigation()
        }

        indexBtnSelectPicture.onDebouncedClick {
            AutoPermissionRequester.with(this)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted {
                        mediaSelector.takeMultiPicture(true, 3)
                    }
                    .request()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaSelector.onActivityResult(requestCode, resultCode, data)
    }

}