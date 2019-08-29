package me.ztiany.arch.home.main.mine.presentation

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
 *      Date : 2018-11-02 14:40
 */
class MineFragment : InjectorBaseFragment() {

    @Inject
    lateinit var mVewModelFactory: ViewModelProvider.Factory

    private lateinit var mMineViewModule: MineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMineViewModule = ViewModelProviders.of(this, mVewModelFactory)[MineViewModel::class.java]
        Timber.d(mMineViewModule.toString())
        Timber.d(mVewModelFactory.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "我的"
            textSize = 30F
            textColor = Color.BLACK
        }
    }

}