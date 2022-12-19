package com.biyun.cg.box.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.fragment.tools.FragmentInfo
import com.android.base.architecture.fragment.tools.TabManager
import com.biyun.cg.box.main.R
import com.biyun.cg.box.main.databinding.MainFragmentMainBinding
import com.biyun.cg.box.main.home.welfare.WelfareFragment
import com.biyun.cg.box.main.home.game.GameFragment
import com.biyun.cg.box.main.home.mine.MineFragment
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 16:06
 */
class MainFragment : BaseUIFragment<MainFragmentMainBinding>() {

    private lateinit var tabManager: MainTabManager

    @SuppressLint("BinaryOperationInTimber")
    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        //tab manager
        tabManager = MainTabManager(requireContext(), childFragmentManager, R.id.flMainContainer)
        tabManager.setup(savedInstanceState)
        //bottomBar
        vb.mainBottomBar.itemIconTintList = null
        vb.mainBottomBar.setOnNavigationItemSelectedListener {
            tabManager.selectTabById(it.itemId)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

    fun selectTabAtPosition(pagePosition: Int) {
        try {
            if (pagePosition in 0..3/*tab count*/) {
                vb.mainBottomBar.selectedItemId = tabManager.getItemId(pagePosition)
            }
        } catch (e: Exception) {
            Timber.d(e, "selectTabAtPosition page=$pagePosition")
        }
    }

}

private class MainTabManager(
    context: Context,
    fragmentManager: FragmentManager,
    containerId: Int
) : TabManager(context, fragmentManager, MainTabs(), containerId) {

    private val itemIdArray = intArrayOf(
        R.id.main_table_game,
        R.id.main_table_welfare,
        R.id.main_table_mine
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs : TabManager.Tabs() {
        init {
            add(FragmentInfo.PageBuilder().clazz(GameFragment::class.java).tag(GameFragment::class.java.name).pagerId(R.id.main_table_game).build())
            add(FragmentInfo.PageBuilder().clazz(WelfareFragment::class.java).tag(WelfareFragment::class.java.name).pagerId(R.id.main_table_welfare).build())
            add(FragmentInfo.PageBuilder().clazz(MineFragment::class.java).tag(MineFragment::class.java.name).pagerId(R.id.main_table_mine).build())
        }
    }

}