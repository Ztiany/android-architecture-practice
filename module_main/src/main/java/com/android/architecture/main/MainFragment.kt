package com.android.architecture.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.architecture.main.databinding.MainFragmentRootBinding
import com.android.architecture.main.feed.FeedFragment
import com.android.architecture.main.me.MeFragment
import com.android.architecture.main.message.MessageFragment
import com.android.base.app.fragment.BaseUIFragment
import com.android.base.app.fragment.tools.FragmentInfo
import com.android.base.app.fragment.tools.TabManager
import com.android.base.app.ui.viewBinding
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 16:06
 */
class MainFragment : BaseUIFragment() {

    interface MainFragmentChild

    private val layout by viewBinding(MainFragmentRootBinding::bind)

    private lateinit var tabManager: MainTabManager

    override fun provideLayout() = R.layout.main_fragment_root

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
        layout.mainBottomBar.setOnNavigationItemSelectedListener {
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
                layout.mainBottomBar.selectedItemId = tabManager.getItemId(pagePosition)
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
        R.id.main_feed,
        R.id.main_message,
        R.id.main_me
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs : TabManager.Tabs() {
        init {
            add(
                FragmentInfo.PageBuilder()
                    .clazz(FeedFragment::class.java)
                    .tag(FeedFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_feed)
                    .build()
            )

            add(
                FragmentInfo.PageBuilder()
                    .clazz(MessageFragment::class.java)
                    .tag(MessageFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_message)
                    .build()
            )

            add(
                FragmentInfo.PageBuilder()
                    .clazz(MeFragment::class.java)
                    .tag(MeFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_me)
                    .build()
            )
        }
    }

}