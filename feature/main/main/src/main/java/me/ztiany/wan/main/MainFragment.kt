package me.ztiany.wan.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.tool.FragmentInfo
import com.android.base.fragment.tool.TabManager
import me.ztiany.wan.main.databinding.MainFragmentMainBinding
import me.ztiany.wan.main.presentation.box.BoxFragment
import me.ztiany.wan.main.presentation.home.HomeFragment
import me.ztiany.wan.main.presentation.mine.MineFragment
import timber.log.Timber

/**
 *@author Ztiany
 */
class MainFragment : BaseUIFragment<MainFragmentMainBinding>() {

    private lateinit var tabManager: MainTabManager

    @SuppressLint("BinaryOperationInTimber")
    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) {
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
            if (pagePosition in 0 until tabManager.tableSize()) {
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
    containerId: Int,
) : TabManager(context, fragmentManager, MainTabs(), containerId, SHOW_HIDE, true) {

    private val itemIdArray = intArrayOf(
        R.id.main_table_home,
        R.id.main_table_box,
        R.id.main_table_mine,
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs : Tabs() {
        init {
            add(FragmentInfo(HomeFragment::class.java, pageId = R.id.main_table_home))
            add(FragmentInfo(BoxFragment::class.java, pageId = R.id.main_table_box))
            add(FragmentInfo(MineFragment::class.java, pageId = R.id.main_table_mine))
        }
    }

}