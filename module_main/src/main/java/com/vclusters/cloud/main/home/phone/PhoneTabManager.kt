package com.vclusters.cloud.main.home.phone

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.android.base.architecture.fragment.tools.FragmentInfo
import com.android.base.architecture.fragment.tools.TabManager
import com.vclusters.cloud.main.R

class PhoneTabManager(
    context: Context,
    fragmentManager: FragmentManager,
    containerId: Int
) : TabManager(context, fragmentManager, PhoneTabs(), containerId) {

    private val itemIdArray = intArrayOf(
        R.id.main_cloud_phone_preview,
        R.id.main_cloud_phone_list
    )

    fun getItemId(position: Int): Int {
        if (position < itemIdArray.size && position >= 0) {
            return itemIdArray[position]
        }
        return -1
    }

    private class PhoneTabs : TabManager.Tabs() {
        init {
            add(
                FragmentInfo.PageBuilder()
                    .clazz(PhonePreviewsFragment::class.java)
                    .tag(PhonePreviewsFragment::class.java.name)
                    .pagerId(R.id.main_cloud_phone_preview)
                    .build()
            )

            add(
                FragmentInfo.PageBuilder()
                    .clazz(PhoneListLayoutFragment::class.java)
                    .tag(PhoneListLayoutFragment::class.java.name)
                    .pagerId(R.id.main_cloud_phone_list)
                    .build()
            )
        }
    }

}