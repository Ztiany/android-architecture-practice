package me.ztiany.wan.main.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.tool.FragmentInfo
import com.android.base.utils.common.unsafeLazy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.R
import me.ztiany.wan.main.databinding.MainFragmentHomeBinding
import me.ztiany.wan.main.presentation.feed.FeedFragment
import me.ztiany.wan.main.presentation.square.SquareFragment

@AndroidEntryPoint
class HomeFragment : BaseUIFragment<MainFragmentHomeBinding>() {

    private val fragmentInfoList by unsafeLazy {
        listOf(
            FragmentInfo(FeedFragment::class.java, title = getString(R.string.main_recommend)),
            FragmentInfo(SquareFragment::class.java, title = getString(R.string.main_square)),
        )
    }

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        // let the homeTabLayout consume the window insets
        ViewCompat.setOnApplyWindowInsetsListener(homeTabLayout) { _, insets ->
            val systemWindowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            homeTabLayout.updatePadding(top = systemWindowInsets.top)
            insets
        }

        mainViewPager2.adapter = HomePagerAdapter(requireContext(), childFragmentManager, lifecycle, fragmentInfoList) { position, fragment ->
            doOnFragmentCreated(position, fragment)
        }
        // TODO: fix ViewPager2's conflict with Banner(implemented by ViewPager)
        mainViewPager2.isUserInputEnabled = false

        TabLayoutMediator(homeTabLayout, mainViewPager2) { tab, position ->
            tab.text = fragmentInfoList[position].title
        }.attach()
    }

    private fun doOnFragmentCreated(position: Int, fragment: Fragment) {
        // do something
    }

}