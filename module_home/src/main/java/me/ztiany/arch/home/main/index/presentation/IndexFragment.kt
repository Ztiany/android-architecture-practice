package me.ztiany.arch.home.main.index.presentation

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.base.app.fragment.BaseFragment
import org.jetbrains.anko.textColor
import timber.log.Timber
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
class IndexFragment : BaseFragment() {

    override fun hasInjector() = true

    @Inject
    lateinit var mVewModelFactory: ViewModelProvider.Factory

    lateinit var mIndexViewModule: IndexViewModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIndexViewModule = ViewModelProviders.of(this, mVewModelFactory)[IndexViewModule::class.java]
        Timber.d(mIndexViewModule.toString())
        Timber.d(mVewModelFactory.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "首页"
            textSize = 30F
            textColor = Color.BLACK
        }
    }

}