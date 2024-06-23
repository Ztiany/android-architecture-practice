package me.ztiany.wan.main.presentation.mine

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.android.base.ui.shape.EnhancedShapeable
import com.android.base.utils.android.views.*
import com.app.base.utils.hidePhoneNumber
import com.app.common.api.usermanager.User
import com.app.common.api.usermanager.isLogin
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import me.ztiany.wan.main.R
import me.ztiany.wan.main.databinding.MainFragmentMineBinding

private class Entrance(
    @IdRes val id: Int,
    @StringRes val name: Int,
    @DrawableRes val icon: Int
)

private fun buildEntranceList() = listOf(
    Entrance(R.id.main_entrance_recharge_record, R.string.main_share_article, R.drawable.main_icon_recharge_record),
    Entrance(R.id.main_entrance_feedback, R.string.main_feedback, R.drawable.main_icon_feedback),
    Entrance(R.id.main_entrance_settings, R.string.main_settings, R.drawable.main_icon_settings),
)

internal class MineUIPresenter(
    private val host: MineFragment,
    private val vb: MainFragmentMineBinding
) {

    fun initLayout() {
        setUpUserInfoLayout()
        setUpEntrances()
    }

    private fun setUpUserInfoLayout() {
        vb.mainTvLogin.onThrottledClickClick {
            host.mainScopeNavigator.toLogin()
        }
    }

    private fun setUpEntrances() {
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
        val entranceTv = entranceView.findChild<TextView>(R.id.main_tv_entrance_name)
        entranceTv.setText(entrance.name)
        entranceTv.setCompoundDrawablesWithIntrinsicBounds(entrance.icon, 0, 0, 0)
        entranceView.findChild<View>(R.id.main_view_entrance_divider).beVisibleOrGone(!last)

        if (first) {
            (entranceView as EnhancedShapeable).shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setTopLeftCorner(CornerFamily.ROUNDED, dip(20F))
                .setTopRightCorner(CornerFamily.ROUNDED, dip(20F))
                .build()
        } else if (last) {
            (entranceView as EnhancedShapeable).shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setBottomLeftCorner(CornerFamily.ROUNDED, dip(20F))
                .setBottomRightCorner(CornerFamily.ROUNDED, dip(20F))
                .build()
        }

        entranceView.onThrottledClickClick {
            processOnEntranceClicked(entrance.id)
        }
    }

    private fun processOnEntranceClicked(id: Int) {
        when (id) {
            R.id.main_entrance_recharge_record -> host.mainScopeNavigator.checkRechargeRecords()
            R.id.main_entrance_settings -> host.mainScopeNavigator.openSettings()
            R.id.main_entrance_feedback -> host.mainScopeNavigator.openFeedback()
        }
    }

    fun showUserInfo(user: User) {
        if (user.isLogin()) {
            vb.mainTvUsername.beVisible()
            vb.mainTvUsername.text = hidePhoneNumber(user.phoneNumber)
            vb.mainGroupLoginOut.beGone()
        } else {
            vb.mainGroupLoginOut.beVisible()
            vb.mainTvUsername.beInvisible()
        }
    }

}