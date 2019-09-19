package me.ztiany.arch.home.main.index.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.utils.android.views.onDebouncedClick
import com.app.base.AppContext
import com.app.base.app.InjectorBaseFragment
import com.app.base.router.RouterPath
import kotlinx.android.synthetic.main.index_fragment.*
import me.ztiany.arch.home.main.MainFragment
import me.ztiany.architecture.home.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
class IndexFragment : InjectorBaseFragment(), MainFragment.MainFragmentChild {

    private val viewModel: IndexViewModule by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        //no op
    }

    override fun provideLayout() = R.layout.index_fragment

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        indexBtnOpenAccount.onDebouncedClick {
            AppContext.appRouter().build(RouterPath.Account.PATH).navigation()
        }
    }

}