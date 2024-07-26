package com.app.base.widget.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.base.fragment.tool.exitFragment
import com.android.base.ui.compat.MaterialToolbar
import com.android.base.utils.android.views.activityContext
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.newMWLayoutParams
import com.android.base.utils.android.views.tintDrawable
import com.android.base.utils.common.requireNonNull
import com.app.base.R
import com.google.android.material.internal.ToolbarUtils
import timber.log.Timber


/**
 * @author Ztiany
 */
class AppTitleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var _toolbar: MaterialToolbar

    private var mOriginalTopPadding = 0

    private var onNavigationOnClickListener: OnClickListener? = null

    val menu: Menu
        get() = toolbar.menu

    val toolbar: MaterialToolbar
        get() = _toolbar

    init {
        context.obtainStyledAttributes(attrs, R.styleable.AppTitleLayout).use {
            fillAttributes(context, it)
        }
    }

    private fun fillAttributes(context: Context, typedArray: TypedArray) {
        // get all attributes
        val title = typedArray.getString(R.styleable.AppTitleLayout_atl_title) ?: ""
        val menuResId = typedArray.getResourceId(R.styleable.AppTitleLayout_atl_menu_id, INVALIDATE_ID)
        val showDivider = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_show_divider, false)
        val cuttingLineBg = typedArray.getColor(
            R.styleable.AppTitleLayout_atl_divider_color,
            ContextCompat.getColor(getContext(), com.app.base.ui.R.color.divider_color)
        )
        val disableNavigation = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_disable_navigation, false)
        val navigationIcon = typedArray.getDrawable(R.styleable.AppTitleLayout_atl_navigation_icon)
        val iconTintColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_navigation_icon_tint, -1)
        val titleColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_title_color, Color.BLACK)
        val menuColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_menu_color, Color.BLACK)
        val titleCentered = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_title_centered, false)

        // set attributes
        mOriginalTopPadding = paddingTop
        orientation = VERTICAL
        if (isInEditMode) {
            mockTitleLayout(title, titleColor, titleCentered, disableNavigation, navigationIcon, iconTintColor)
            fitStatusInset()
        } else {
            fitStatusInset()
            inflate(context, R.layout.widget_title_layout, this)
            //get resource
            iniToolbar(title, titleCentered, showDivider, titleColor, cuttingLineBg)
            //icon
            initNavigationIcon(disableNavigation, navigationIcon, iconTintColor)
            //menu
            initMenu(menuResId, menuColor)
        }
    }

    private fun mockTitleLayout(
        title: String,
        titleColor: Int,
        titleCentered: Boolean,
        disableNavigation: Boolean,
        navigationIcon: Drawable?,
        iconTintColor: Int,
    ) {
        val child = TextView(context)
        child.textSize = 18f
        child.text = title
        child.setTextColor(titleColor)
        val layoutParams = newMWLayoutParams()
        layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, resources.displayMetrics).toInt()
        addView(child, layoutParams)

        if (titleCentered) {
            child.gravity = Gravity.CENTER
        } else {
            child.setPadding(dip(15), 0, 0, 0)
        }
        if (disableNavigation) {
            return
        }
        if (navigationIcon != null) {
            if (iconTintColor == -1) {
                child.setCompoundDrawablesRelative(navigationIcon, null, null, null)
            } else {
                child.setCompoundDrawablesRelative(tintDrawable(navigationIcon.mutate(), iconTintColor), null, null, null)
            }
        } else {
            if (iconTintColor == -1) {
                child.setCompoundDrawablesRelativeWithIntrinsicBounds(com.app.base.ui.R.drawable.icon_back, 0, 0, 0)
            } else {
                val drawable = ContextCompat.getDrawable(context, com.app.base.ui.R.drawable.icon_back)
                val tintedDrawable = tintDrawable(requireNonNull(drawable).mutate(), iconTintColor)
                child.setCompoundDrawablesRelative(tintedDrawable, null, null, null)
            }
        }
    }

    private fun initMenu(menuResId: Int, menuColor: Int) {
        if (menuResId != INVALIDATE_ID) {
            toolbar.inflateMenu(menuResId)
            setMenuColor(menuColor)
        }
    }

    private fun initNavigationIcon(disableNavigation: Boolean, navigationIcon: Drawable?, iconTintColor: Int) {
        if (disableNavigation) {
            return
        }

        if (navigationIcon != null) {
            if (iconTintColor == -1) {
                toolbar.navigationIcon = navigationIcon
            } else {
                toolbar.navigationIcon = tintDrawable(navigationIcon.mutate(), iconTintColor)
            }
        } else {
            if (iconTintColor == -1) {
                toolbar.setNavigationIcon(com.app.base.ui.R.drawable.icon_back)
            } else {
                toolbar.navigationIcon = tintDrawable(
                    requireNonNull(
                        ContextCompat.getDrawable(
                            context,
                            com.app.base.ui.R.drawable.icon_back
                        )
                    ).mutate(), iconTintColor
                )
            }
        }
    }

    private fun iniToolbar(title: String, titleCentered: Boolean, showDivider: Boolean, titleColor: Int, cuttingLineBg: Int) {
        _toolbar = findViewById(R.id.atl_toolbar)
        // divider
        val divider = findViewById<View>(R.id.widget_app_title_divider)
        divider.visibility = if (showDivider) VISIBLE else GONE
        divider.setBackgroundColor(cuttingLineBg)
        // navigation
        toolbar.setContentInsetStartWithNavigation(0)
        toolbar.isTitleCentered = titleCentered
        toolbar.setNavigationOnClickListener { v: View -> this.onNavigationOnClick(v) }
        if (background != null) {
            toolbar.background = background
        }
        //title
        toolbar.setTitle(title)
        toolbar.setTitleTextColor(titleColor)
    }

    private fun fitStatusInset() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            Timber.d("AppTitleLayout(${(activityContext)}) fitStatusInset is executed:  systemBars = $systemBars}")
            setPadding(
                paddingLeft,
                mOriginalTopPadding + systemBars.top,
                paddingRight,
                paddingBottom
            )
            insets
        }
    }

    fun setNavigationIcon(@DrawableRes iconId: Int) {
        toolbar.navigationIcon = ContextCompat.getDrawable(context, iconId)
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }

    fun setTitle(@StringRes titleId: Int) {
        toolbar.title = context.getString(titleId)
    }

    fun setOnNavigationOnClickListener(onNavigationOnClickListener: OnClickListener?) {
        this.onNavigationOnClickListener = onNavigationOnClickListener
    }

    private fun onNavigationOnClick(v: View) {
        onNavigationOnClickListener?.let {
            it.onClick(v)
            return
        }
        val realContext = this.activityContext
        if (realContext != null) {
            realContext.exitFragment(false)
        } else {
            Timber.w("perform onNavigationOnClick --> fragmentBack, but real context can not be found")
        }
    }

    @SuppressLint("RestrictedApi")
    fun findMenuView(@IdRes menuId: Int): View? {
        return ToolbarUtils.getActionMenuItemView(toolbar, menuId)
    }

    fun setMenuColor(@ColorInt color: Int) {
        setMenuColor(color, "")
    }

    @SuppressLint("RestrictedApi")
    fun setMenuColor(@ColorInt color: Int, target: String) {
        var view: View?
        var innerView: View?

        for (i in 0 until toolbar.childCount) {
            view = toolbar.getChildAt(i)
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

    companion object {
        private const val INVALIDATE_ID = -1
    }

}