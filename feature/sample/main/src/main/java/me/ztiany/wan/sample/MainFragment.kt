package me.ztiany.wan.sample

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.tool.FragmentInfo
import com.android.base.fragment.tool.TabManager
import me.ztiany.wan.sample.databinding.SampleFragmentMainBinding

/**
 * @author Ztiany
 */
class MainFragment : BaseUIFragment<SampleFragmentMainBinding>() {

    private lateinit var tabManager: MainTabManager

    @SuppressLint("BinaryOperationInTimber")
    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        //tab manager
        tabManager = MainTabManager(requireContext(), childFragmentManager, R.id.sample_fl_container)
        tabManager.setup(savedInstanceState)
        //bottomBar
        sampleBottomBar.itemIconTintList = null
        sampleBottomBar.clipToOutline = true
        sampleBottomBar.setOnItemSelectedListener {
            tabManager.selectTabById(it.itemId)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

}

private class MainTabManager(
    context: Context,
    fragmentManager: FragmentManager,
    containerId: Int,
) : TabManager(context, fragmentManager, MainTabs(), containerId, SHOW_HIDE, true) {

    private val itemIdArray = intArrayOf(
        R.id.sample_table_pager,
        R.id.sample_table_component
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class MainTabs : Tabs() {
        init {
            add(FragmentInfo(PagerArchitectureFragment::class.java, pageId = R.id.sample_table_pager))
            add(FragmentInfo(UIComponentFragment::class.java, pageId = R.id.sample_table_component))
        }
    }

}