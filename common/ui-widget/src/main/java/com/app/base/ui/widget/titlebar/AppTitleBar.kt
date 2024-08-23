package com.app.base.ui.widget.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.core.content.res.use
import com.android.base.fragment.tool.exitFragment
import com.android.base.utils.android.views.activityContext
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.widget.R
import com.google.android.material.internal.ToolbarUtils
import timber.log.Timber

/**
 * @author Ztiany
 */
class AppTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.toolbarStyle,
) : com.google.android.material.appbar.MaterialToolbar(context, attrs, defStyleAttr) {

    private var showDivider = false

    private var dividerColor = Color.BLACK

    private var disableEventHandling = false

    private val dividerPaint by unsafeLazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = 1F }
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.AppTitleBar).use {
            showDivider = it.getBoolean(R.styleable.AppTitleBar_atbShowDivider, false)
            dividerColor = it.getColor(R.styleable.AppTitleBar_atbDividerColor, Color.BLACK)
        }

        setContentInsetStartWithNavigation(0)
        setNavigationOnClickListener { v: View -> this.onNavigationOnClick(v) }
    }

    private fun onNavigationOnClick(v: View) {
        activityContext?.run {
            exitFragment(false)
        } ?: Timber.w("activity context can not be found.")
    }

    @SuppressLint("RestrictedApi")
    fun findMenuView(@IdRes menuId: Int): View? {
        return ToolbarUtils.getActionMenuItemView(this, menuId)
    }

    @SuppressLint("RestrictedApi")
    fun setMenuColor(@ColorInt color: Int, target: String) {
        var view: View?
        var innerView: View?

        for (i in 0 until childCount) {
            view = getChildAt(i)
            if (view is ActionMenuView) {
                for (j in 0 until view.childCount) {
                    innerView = view.getChildAt(j)
                    if (!TextUtils.isEmpty(target)) {
                        if ((innerView is ActionMenuItemView) && innerView.text == target) {
                            innerView.setTextColor(color)
                            break
                        }
                    } else {
                        if (innerView is ActionMenuItemView) {
                            innerView.setTextColor(color)
                        }
                    }
                }
                break
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (disableEventHandling) {
            return false
        }
        return super.onTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        dividerPaint.setColor(dividerColor)
        if (showDivider) {
            canvas.drawLine(
                paddingLeft.toFloat(),
                (height - paddingBottom).toFloat(),
                (width - paddingRight).toFloat(),
                (height - paddingBottom).toFloat(),
                dividerPaint,
            )
        }
    }

}