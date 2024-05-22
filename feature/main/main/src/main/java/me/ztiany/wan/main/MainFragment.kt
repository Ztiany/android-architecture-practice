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
import me.ztiany.wan.main.presentation.feed.FeedFragment
import me.ztiany.wan.main.presentation.mine.MineFragment
import me.ztiany.wan.main.presentation.box.BoxFragment
import timber.log.Timber

/**
 *@author Ztiany
 */
class MainFragment : BaseUIFragment<MainFragmentMainBinding>() {

    private lateinit var tabManager: MainTabManager

    @SuppressLint("BinaryOperationInTimber")
    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) {
        super.onSetUpCreatedView(view, savedInstanceState)
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
) : TabManager(context, fragmentManager, MainTabs(), containerId, SHOW_HIDE, true) {

    private val itemIdArray = intArrayOf(
        R.id.main_table_game,
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
            add(FragmentInfo.PageBuilder().clazz(FeedFragment::class.java).tag(FeedFragment::class.java.name).pagerId(R.id.main_table_game).build())
            add(FragmentInfo.PageBuilder().clazz(BoxFragment::class.java).tag(BoxFragment::class.java.name).pagerId(R.id.main_table_box).build())
            add(FragmentInfo.PageBuilder().clazz(MineFragment::class.java).tag(MineFragment::class.java.name).pagerId(R.id.main_table_mine).build())
        }
    }

}