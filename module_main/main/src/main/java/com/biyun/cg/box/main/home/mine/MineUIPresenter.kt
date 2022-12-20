package com.biyun.cg.box.main.home.mine

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.android.base.ui.drawable.Corner
import com.android.base.ui.shape.EnhancedShapeable
import com.android.base.utils.android.views.*
import com.biyun.cg.box.main.R
import com.biyun.cg.box.main.databinding.MainFragmentMineBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

private class Entrance(
    @IdRes val id: Int,
    @StringRes val name: Int,
    @DrawableRes val icon: Int
)

private fun buildEntranceList() = listOf(
    Entrance(R.id.main_entrance_recharge_record, R.string.main_recharge_record, R.drawable.icon_settings),
    Entrance(R.id.main_entrance_settings, R.string.main_settings, R.drawable.icon_settings),
    Entrance(R.id.main_entrance_feedback, R.string.main_help_feedback, R.drawable.icon_settings),
)

internal class MineUIPresenter(
    private val host: MineFragment,
    private val vb: MainFragmentMineBinding
) {

    fun initLayout() {
        val inflater = LayoutInflater.from(host.requireContext())
        val entranceList = buildEntranceList()
        val lastIndex = entranceList.size - 1
        entranceList.forEachIndexed { index, entrance ->
            val entranceView = inflater.inflate(R.layout.main_item_mine_entrance, vb.mainLlEntrances, false)
            vb.mainLlEntrances.addView(entranceView)
            bindEntranceInfo(entrance, entranceView, index == 0, index == lastIndex)
        }
    }

    private fun bindEntranceInfo(entrance: Entrance, entranceView: View, first: Boolean, last: Boolean) {
        val entranceTv = entranceView.find<TextView>(R.id.main_tv_entrance_name)
        entranceTv.setText(entrance.name)
        entranceTv.setCompoundDrawablesWithIntrinsicBounds(entrance.icon, 0, 0, 0)
        entranceView.find<View>(R.id.main_view_entrance_divider).visibleOrGone(!last)
        if (first) {
            (entranceView as EnhancedShapeable).shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setTopLeftCorner(CornerFamily.ROUNDED, dpToPx(10F))
                .setTopRightCorner(CornerFamily.ROUNDED, dpToPx(10F))
                .build()
        } else if (last) {
            (entranceView as EnhancedShapeable).shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setBottomLeftCorner(CornerFamily.ROUNDED, dpToPx(10F))
                .setBottomRightCorner(CornerFamily.ROUNDED, dpToPx(10F))
                .build()
        }
        entranceView.onDebouncedClick {
            processOnEntranceClicked(entrance.id)
        }
    }

    private fun processOnEntranceClicked(id: Int) {
        host.showMessage("开发中......")
    }

}