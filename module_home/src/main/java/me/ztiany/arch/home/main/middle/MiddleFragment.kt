package me.ztiany.arch.home.main.middle

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.app.base.app.AppBaseFragment
import com.app.base.app.InjectorBaseFragment
import org.jetbrains.anko.textColor
import timber.log.Timber
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:38
 */
class MiddleFragment : InjectorBaseFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    private lateinit var mMiddleViewModel: MiddleViewModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMiddleViewModel = ViewModelProviders.of(this, mViewModelFactory)[MiddleViewModule::class.java]
        Timber.d(mViewModelFactory.toString())
        Timber.d(mMiddleViewModel.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "陪伴"
            textSize = 30F
            textColor = Color.BLACK
        }
    }

}