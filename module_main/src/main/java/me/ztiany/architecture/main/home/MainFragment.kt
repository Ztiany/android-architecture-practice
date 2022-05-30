package me.ztiany.architecture.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.fragment.tools.FragmentInfo
import com.android.base.architecture.fragment.tools.TabManager
import me.ztiany.architecture.main.R
import me.ztiany.architecture.main.databinding.MainFragmentMainBinding
import me.ztiany.architecture.main.home.android.AndroidFragment
import me.ztiany.architecture.main.home.business.BusinessFragment
import me.ztiany.architecture.main.home.interfaces.MineFragment
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
        R.id.main_business,
        R.id.main_android,
        R.id.main_interface
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs : TabManager.Tabs() {
        init {
            add(FragmentInfo.PageBuilder().clazz(BusinessFragment::class.java).tag(BusinessFragment::class.java.name).pagerId(R.id.main_business).build())
            add(FragmentInfo.PageBuilder().clazz(AndroidFragment::class.java).tag(AndroidFragment::class.java.name).pagerId(R.id.main_android).build())
            add(FragmentInfo.PageBuilder().clazz(MineFragment::class.java).tag(MineFragment::class.java.name).pagerId(R.id.main_interface).build())
        }
    }

}