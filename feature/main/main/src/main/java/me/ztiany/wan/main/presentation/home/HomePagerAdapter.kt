package me.ztiany.wan.main.presentation.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.fragment.tool.FragmentInfo

class HomePagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<FragmentInfo>,
    private val onFragmentCreated: (position: Int, fragment: Fragment) -> Unit = { _, _ -> },
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].newFragment(context).also {
            onFragmentCreated(position, it)
        }
    }

}