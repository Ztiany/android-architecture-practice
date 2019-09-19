package me.ztiany.arch.home.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.app.fragment.BaseFragment
import com.android.base.app.fragment.FragmentInfo
import com.android.base.app.fragment.TabManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_fragment_root.*
import me.ztiany.arch.home.main.index.presentation.IndexFragment
import me.ztiany.arch.home.main.middle.MiddleFragment
import me.ztiany.arch.home.main.mine.presentation.MineFragment
import me.ztiany.architecture.home.R
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 16:06
 */
class MainFragment : BaseFragment() {

    interface MainFragmentChild

    private lateinit var bottomBar: BottomNavigationView

    private lateinit var tabManager: MainTabManager

    override fun provideLayout() = R.layout.main_fragment_root

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        bottomBar = mainBottomBar
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        //tab manager
        tabManager = MainTabManager(requireContext(), childFragmentManager, R.id.flMainContainer)
        tabManager.setup(savedInstanceState)
        //bottomBar
        bottomBar.setOnNavigationItemSelectedListener {
            tabManager.selectTabById(it.itemId)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

    fun selectTabAtPosition(page: Int) {
        try {
            if (page in 0..3) {
                mainBottomBar.selectedItemId = tabManager.getItemId(page)
            }
        } catch (e: Exception) {
            Timber.d(e, "selectTabAtPosition page=$page")
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            childFragmentManager.fragments.forEach {
                if (!it.isHidden) {
                    it.onHiddenChanged(hidden)
                }
            }
        }
    }

}

private class MainTabManager(
        context: Context,
        fragmentManager: FragmentManager,
        containerId: Int
) : TabManager(context, fragmentManager, MainTabs(), containerId, SHOW_HIDE) {

    private val itemIdArray = intArrayOf(
            R.id.main_index,
            R.id.main_middle,
            R.id.main_mine
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs internal constructor() : TabManager.Tabs() {
        init {
            add(FragmentInfo.PageBuilder()
                    .clazz(IndexFragment::class.java)
                    .tag(IndexFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_index)
                    .build())

            add(FragmentInfo.PageBuilder()
                    .clazz(MiddleFragment::class.java)
                    .tag(MiddleFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_middle)
                    .build())

            add(FragmentInfo.PageBuilder()
                    .clazz(MineFragment::class.java)
                    .tag(MineFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_mine)
                    .build())
        }
    }

}