package com.vclusters.cloud.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.fragment.tools.FragmentInfo
import com.android.base.architecture.fragment.tools.TabManager
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentMainBinding
import com.vclusters.cloud.main.home.assistant.AssistantFragment
import com.vclusters.cloud.main.home.mine.MineFragment
import com.vclusters.cloud.main.home.phone.PhoneRootFragment
import timber.log.Timber

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 16:06
 */
class MainFragment : BaseUIFragment<MainFragmentMainBinding>() {

    interface MainFragmentChild

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
        viewBinding.mainBottomBar.itemIconTintList = null
        viewBinding.mainBottomBar.setOnNavigationItemSelectedListener {
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
                viewBinding.mainBottomBar.selectedItemId = tabManager.getItemId(pagePosition)
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
        R.id.main_cloud_phone,
        R.id.main_cloud_assistant,
        R.id.main_mine
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
                    .clazz(PhoneRootFragment::class.java)
                    .tag(PhoneRootFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_cloud_phone)
                    .build()
            )

            add(
                FragmentInfo.PageBuilder()
                    .clazz(AssistantFragment::class.java)
                    .tag(AssistantFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_cloud_assistant)
                    .build()
            )

            add(
                FragmentInfo.PageBuilder()
                    .clazz(MineFragment::class.java)
                    .tag(MineFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_mine)
                    .build()
            )
        }
    }

}