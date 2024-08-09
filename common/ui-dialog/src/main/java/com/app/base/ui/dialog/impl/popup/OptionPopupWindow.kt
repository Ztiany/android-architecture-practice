package com.app.base.ui.dialog.impl.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow.OnDismissListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.utils.android.activityContext
import com.android.base.utils.android.views.dip
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.AppPopupWindow
import com.app.base.ui.dialog.databinding.PopupLayoutOptionBinding
import com.app.base.ui.dialog.dsl.Direction
import com.app.base.ui.dialog.dsl.PopupDimDescription
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowInterface
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.google.android.material.shape.TriangleEdgeTreatment
import timber.log.Timber

internal class OptionPopupWindow(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    private val description: OptionPopupWindowDescription,
) : AppPopupWindow<OptionPopupWindowInterface>(context), OptionPopupWindowInterface {

    private val vb = PopupLayoutOptionBinding.inflate(LayoutInflater.from(context))

    private val onDismissListeners: MutableList<OnDismissListener> = mutableListOf()

    private val internalOnDismissListener = OnDismissListener {
        onDismissListeners.forEach { listener ->
            listener.onDismiss()
        }
        recoverTargetView()
    }

    private val dialogInterfaceWrapper by unsafeLazy {
        DialogInterfaceWrapper(this)
    }

    private var originalBackground: Drawable? = null
    private var originalNavigationColor: Int? = null

    override val controller: OptionPopupWindowInterface
        get() = this

    init {
        contentView = vb.root
        description.size.applyTo(this)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setAttributes()
        rememberWidgetState()

        setUpList()
        setContainerBackground()

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dismiss()
            }
        })
    }

    private fun setAttributes() {
        description.behavior.applyTo(this)
        setOnDismissListener(internalOnDismissListener)
        description.behavior.onDismissListener?.let {
            setOnDismissListener {
                it.invoke(dialogInterfaceWrapper.canceledByUser)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // list and indicator
    ///////////////////////////////////////////////////////////////////////////

    private fun setUpList() {
        description.customizeList?.let {
            it.invoke(vb.popupRvList)
            return
        }

        val optionList = description.optionList ?: return

        with(vb.popupRvList) {
            layoutManager = LinearLayoutManager(context)
            adapter = OptionPopupWindowAdapter(
                context,
                optionList.itemStyle,
                optionList.items
            ) { position: Int, item: CharSequence ->
                optionList.onOptionSelectedListener(dialogInterfaceWrapper, position, item)
                dismissChecked()
            }
        }
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    private fun setContainerBackground() = with(vb.popupFlBg) {
        val cornerSize = dip(5F)

        val shapeAppearanceModel = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(cornerSize)
            applyIndicatorStyle(cornerSize)
        }.build()

        background = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(MaterialColors.getColor(context, com.app.base.ui.theme.R.attr.app_color_lightest, "app_color_lightest not provided."))
        }
    }

    private fun ShapeAppearanceModel.Builder.applyIndicatorStyle(cornerSize: Float) {
        when (description.indicator.direction) {
            Direction.TOP -> {
                setTopEdge(buildEdgeTreatment(cornerSize, false))
            }

            Direction.BOTTOM -> {
                setBottomEdge(buildEdgeTreatment(cornerSize, true))
            }

            Direction.LEFT -> {
                setLeftEdge(buildEdgeTreatment(cornerSize, false))
            }

            Direction.RIGHT -> {
                setRightEdge(buildEdgeTreatment(cornerSize, false))
            }
        }
    }

    /**
     * @see TriangleEdgeTreatment
     */
    private fun buildEdgeTreatment(cornerSize: Float, reverse: Boolean) = object : EdgeTreatment() {

        private val triangleSize = dip(10F)

        private val other = TriangleEdgeTreatment(triangleSize, false)

        override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
            val offsetFromStart = if (reverse) description.indicator.offsetFromEnd else description.indicator.offsetFromStart
            val offsetFromEnd = if (reverse) description.indicator.offsetFromStart else description.indicator.offsetFromEnd

            Timber.d("getEdgePath() called with: length = $length, center = $center, interpolation = $interpolation, shapePath = $shapePath")

            val fixedCenter = if (offsetFromStart != -1 && offsetFromEnd == -1) {
                offsetFromStart + triangleSize / 2F + cornerSize
            } else if (offsetFromStart == -1 && offsetFromEnd != -1) {
                center + center - offsetFromEnd - triangleSize / 2F - cornerSize
            } else {
                center
            }
            other.getEdgePath(length, fixedCenter, interpolation, shapePath)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // dim target view
    ///////////////////////////////////////////////////////////////////////////

    override fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        if (onDismissListener == internalOnDismissListener) {
            super.setOnDismissListener(onDismissListener)
        } else if (onDismissListener != null) {
            onDismissListeners.add(onDismissListener)
        }
    }

    override fun showAsDropDown(anchor: View) {
        super.showAsDropDown(anchor)
        dimTargetView()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        super.showAsDropDown(anchor, xoff, yoff)
        dimTargetView()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        dimTargetView()
    }

    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        dimTargetView()
    }

    private fun rememberWidgetState() {
        description.popupDim?.let {
            originalBackground = it.dimView?.background
            context.activityContext?.window?.navigationBarColor?.let { color ->
                originalNavigationColor = color
            }
        }
    }

    private fun dimTargetView() {
        val dimDescription = description.popupDim ?: return

        dimDescription.dimView?.let {
            it.background = ColorDrawable(dimDescription.dimColor)
            if (dimDescription.includeNavigationBar) {
                context.activityContext?.window?.navigationBarColor = dimDescription.dimColor
            }
            return
        }

        updateDimAmount(dimDescription.dimPercent)
    }

    private fun recoverTargetView() {
        val dimDescription = description.popupDim ?: return
        dimDescription.dimView?.let {
            it.background = originalBackground
            if (dimDescription.includeNavigationBar) {
                originalNavigationColor?.let { color ->
                    context.activityContext?.window?.navigationBarColor = color
                }
            }
        }
    }

    private fun updateDimAmount(percent: Float) {
        if (percent == PopupDimDescription.NO_DIM) return
        getDecorView()?.let {
            val layoutParams = it.layoutParams as WindowManager.LayoutParams
            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            layoutParams.dimAmount = percent
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).updateViewLayout(it, layoutParams)
        }
    }

    private fun getDecorView(): View? {
        var decorView: View? = null
        try {
            decorView = if (background == null) {
                contentView.parent as View
            } else {
                contentView.parent.parent as View
            }
        } catch (ignore: Exception) {
        }
        return decorView
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface Implementation
    ///////////////////////////////////////////////////////////////////////////
    override fun cancel() {
        dismiss()
    }

}