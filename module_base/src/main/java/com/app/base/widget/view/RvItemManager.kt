package com.app.base.widget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.KtViewHolder
import com.android.base.adapter.recycler.MultiTypeAdapter
import com.android.base.adapter.recycler.SimpleItemViewBinder
import com.android.base.foundation.adapter.DataManager
import com.android.base.imageloader.ImageLoaderFactory
import com.android.base.imageloader.Source
import com.android.base.interfaces.TextWatcherAdapter
import com.android.base.utils.android.SoftKeyboardUtils
import com.android.base.utils.android.views.*
import com.app.base.R
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.widget_items_item_avatar.*
import kotlinx.android.synthetic.main.widget_items_item_edit_text.*
import kotlinx.android.synthetic.main.widget_items_item_normal.*
import kotlinx.android.synthetic.main.widget_items_switch.*


/**
 * items(like entrance list)，for some reason, you could need to use [FixRecyclerView] for the items.
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-04-12 16:07
 */
class RvItemManager(context: Context, private val baseConfig: ItemConfiguration) {

    private val textItemItemBinder = TextItemViewBinder(baseConfig)
    private val editTextItemItemBinder = EditTextItemViewBinder(baseConfig)
    private val avatarItemItemBinder = AvatarItemViewBinder(baseConfig)
    private val switchItemItemBinder = SwitchItemViewBinder(baseConfig)

    private val itemAdapter = MultiTypeAdapter(context)
    private var recyclerView: RecyclerView? = null

    private var onItemClickedListener: ((View, BaseItem) -> Unit)? = null
    private var onSwitchItemChangedListener: ((SwitchItem, Boolean) -> Unit)? = null

    fun setOnTextItemClickedListener(onItemClickedListener: ((View, BaseItem) -> Unit)) {
        this.onItemClickedListener = onItemClickedListener
        textItemItemBinder.onItemClicked = onItemClickedListener
    }

    fun setOnSwitchItemChangedListener(onSwitchItemChangedListener: ((SwitchItem, Boolean) -> Unit)) {
        this.onSwitchItemChangedListener = onSwitchItemChangedListener
        switchItemItemBinder.onItemSwitched = onSwitchItemChangedListener
    }

    init {
        itemAdapter.register(textItemItemBinder)
        itemAdapter.register(editTextItemItemBinder)
        itemAdapter.register(avatarItemItemBinder)
        itemAdapter.register(switchItemItemBinder)
    }

    fun <T : BaseItem> registerItem(clazz: Class<T>, baseItemViewBinder: SimpleItemViewBinder<T>) {
        itemAdapter.register(clazz, baseItemViewBinder)
    }

    fun setup(recyclerView: RecyclerView, list: List<BaseItem>) {
        if (this.recyclerView != recyclerView) {
            this.recyclerView = recyclerView
            recyclerView.addItemDecoration(EntranceItemDecoration(baseConfig))
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            itemAdapter.setDataSource(list, false)
            recyclerView.adapter = itemAdapter
        }
    }

    fun replaceItems(list: List<BaseItem>) {
        itemAdapter.setDataSource(list, true)
    }

    @Suppress("UNCHECKED_CAST")
    fun itemManager(): DataManager<BaseItem> = itemAdapter as DataManager<BaseItem>

    fun <T : BaseItem> updateItem(id: Int, logic: (T) -> Unit) {
        itemAdapter.baseItem().find {
            id == it.id
        }?.let {
            @Suppress("UNCHECKED_CAST")
            logic(it as T)
            itemAdapter.notifyItemChanged(itemAdapter.indexItem(it))
        }
    }

    fun updateTextItem(id: Int, logic: (TextItem) -> Unit) {
        itemAdapter.baseItem().find {
            id == it.id
        }?.let {
            logic(it as TextItem)
            itemAdapter.notifyItemChanged(itemAdapter.indexItem(it))
        }
    }

    fun updateAvatarItem(id: Int, logic: (AvatarItem) -> Unit) {
        itemAdapter.baseItem().find {
            id == it.id
        }?.let {
            logic(it as AvatarItem)
            itemAdapter.notifyItemChanged(itemAdapter.indexItem(it))
        }
    }

    fun updateItems(logic: (BaseItem) -> Unit) {
        itemAdapter.baseItem().let {
            it.forEach(logic)
            itemAdapter.notifyDataSetChanged()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun MultiTypeAdapter.baseItem(): List<BaseItem> {
        return (items as? List<BaseItem>) ?: emptyList()
    }

    @Suppress("UNCHECKED_CAST")
    fun items(): List<BaseItem> = itemAdapter.items as List<BaseItem>

    fun scrollToId(id: Int) {
        recyclerView?.smoothScrollToPosition(items().indexOfFirst { it.id == id })
    }

    fun removeItem(id: Int) {
        items().find {
            it.id == id
        }?.let {
            itemAdapter.remove(it)
        }
    }

}

private const val TYPE_TEXT = 1
private const val TYPE_AVATAR = 2
private const val TYPE_EDITTEXT = 3
private const val TYPE_SWITCH = 4

interface BaseItem {
    val id: Int
    val type: Int
    val title: String
    var hasTopCuttingLine: Boolean
    var hasBottomCuttingLine: Boolean
    var topMargin: Int
    var attachment: Any?
}

data class TextItem(
        //inherited
        override val id: Int,
        override val title: String,
        override var hasTopCuttingLine: Boolean = false,
        override var hasBottomCuttingLine: Boolean = false,
        override var topMargin: Int = 0,
        override var attachment: Any? = null,
        //own
        var enable: Boolean = true,
        var hasNotification: Boolean = false,
        var subtitle: String? = null,
        var titleInMiddle: Boolean = false,
        var hasArrow: Boolean = true,
        @ColorInt var subtitleColor: Int = 0,
        var subtitleSize: Int = 0,
        val leftImgId: Int = 0
) : BaseItem {
    override val type: Int = TYPE_TEXT
}

data class EditableTextItem(
        //inherited
        override val id: Int,
        override val title: String,
        override var hasTopCuttingLine: Boolean = false,
        override var hasBottomCuttingLine: Boolean = false,
        override var topMargin: Int = 0,
        override var attachment: Any? = null,
        //own
        val inputType: Int,
        var editable: Boolean = true,
        var content: String? = null,
        var hint: String? = null,
        @ColorInt var editColor: Int = 0,
        @ColorInt var editHintColor: Int = 0,
        var subtitleSize: Int = 0,
        val leftImgId: Int = 0
) : BaseItem {
    override val type: Int = TYPE_EDITTEXT
}

data class AvatarItem(
        //inherited
        override val id: Int,
        override val title: String,
        override var hasTopCuttingLine: Boolean = false,
        override var hasBottomCuttingLine: Boolean = false,
        override var topMargin: Int = 0,
        override var attachment: Any? = null,
        //own
        var hasArrow: Boolean = false,
        val avatarSize: Int = dip(40),
        var showAvatar: ((ImageView) -> Unit)? = null,
        var source: Source? = null
) : BaseItem {
    override val type: Int = TYPE_AVATAR
}

data class SwitchItem(
        //inherited
        override val id: Int,
        override val title: String,
        override var hasTopCuttingLine: Boolean = false,
        override var hasBottomCuttingLine: Boolean = false,
        override var topMargin: Int = 0,
        override var attachment: Any? = null,
        //own
        var isOpen: Boolean
) : BaseItem {
    override val type: Int = TYPE_SWITCH
}

data class ItemConfiguration(
        @ColorInt val cuttingLineColor: Int = Color.BLACK,
        val cuttingLineWidth: Float = 1F,
        val itemTopPadding: Int = dip(14),
        val itemBottomPadding: Int = dip(14),
        val itemStartPadding: Int = dip(13),
        val itemEndPadding: Int = dip(13),
        val paddingLine: Boolean = false
)

class TextItemViewBinder(private val baseConfig: ItemConfiguration) : SimpleItemViewBinder<TextItem>() {

    var onItemClicked: ((View, BaseItem) -> Unit)? = null

    private val onItemListener = View.OnClickListener {
        SoftKeyboardUtils.hideSoftInput(it.realContext)
        onItemClicked?.invoke(it, it.tag as BaseItem)
    }

    override fun provideLayout(inflater: LayoutInflater, parent: ViewGroup) = R.layout.widget_items_item_normal

    override fun onViewHolderCreated(viewHolder: KtViewHolder) {
        super.onViewHolderCreated(viewHolder)
        viewHolder.itemView.setPadding(
                baseConfig.itemStartPadding,
                baseConfig.itemTopPadding,
                baseConfig.itemEndPadding,
                baseConfig.itemBottomPadding
        )
    }

    override fun onBindViewHolder(holder: KtViewHolder, item: TextItem) {
        if (item.titleInMiddle) {
            holder.tvWidgetItemTitleInMiddle.text = item.title
            holder.tvWidgetItemTitle.invisible()
            holder.tvWidgetItemTitleInMiddle.visible()
            holder.viewWidgetItemRedDot.gone()
        } else {
            holder.tvWidgetItemTitle.text = item.title
            holder.tvWidgetItemTitle.visible()
            holder.tvWidgetItemTitleInMiddle.invisible()
            holder.viewWidgetItemRedDot.visibleOrGone(item.hasNotification)
        }

        holder.tvWidgetItemSubtitle.text = item.subtitle
        holder.ivWidgetItemArrow.visibleOrGone(item.hasArrow)

        if (item.subtitleColor != 0) {
            holder.tvWidgetItemSubtitle.setTextColor(item.subtitleColor)
        }
        if (item.subtitleSize > 0) {
            holder.tvWidgetItemSubtitle.textSize = item.subtitleSize.toFloat()
        }
        if (item.leftImgId > 0) {
            holder.tvWidgetItemTitle.setLeftDrawable(item.leftImgId)
        } else {
            holder.tvWidgetItemTitle.clearComponentDrawable()
        }

        if (item.enable) {
            holder.itemView.tag = item
            holder.itemView.setOnClickListener(onItemListener)
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }
}

class EditTextItemViewBinder(private val baseConfig: ItemConfiguration) : SimpleItemViewBinder<EditableTextItem>() {

    override fun provideLayout(inflater: LayoutInflater, parent: ViewGroup) = R.layout.widget_items_item_edit_text

    override fun onViewHolderCreated(viewHolder: KtViewHolder) {
        super.onViewHolderCreated(viewHolder)
        viewHolder.itemView.setPadding(
                baseConfig.itemStartPadding,
                baseConfig.itemTopPadding,
                baseConfig.itemEndPadding,
                baseConfig.itemBottomPadding
        )

        viewHolder.etWidgetItemSubtitle.addTextChangedListener(object : TextWatcherAdapter {
            override fun afterTextChanged(s: Editable?) {
                (viewHolder.etWidgetItemSubtitle?.tag as? EditableTextItem)?.content = s?.toString()
            }
        })
    }

    override fun onViewDetachedFromWindow(holder: KtViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val tag = holder.itemView.tag
        if (tag is EditableTextItem) {
            if (holder.etWidgetItemSubtitle.hasFocus()) {
                SoftKeyboardUtils.hideSoftInput(holder.etWidgetItemSubtitle)
            }
        }
    }

    override fun onBindViewHolder(holder: KtViewHolder, item: EditableTextItem) {
        holder.tvWidgetItemEtTitle.text = item.title
        if (item.leftImgId > 0) {
            holder.tvWidgetItemEtTitle.setLeftDrawable(item.leftImgId)
        } else {
            holder.tvWidgetItemEtTitle.clearComponentDrawable()
        }

        if (item.editColor != 0) {
            holder.etWidgetItemSubtitle.setTextColor(item.editColor)
        }
        if (item.editHintColor != 0) {
            holder.etWidgetItemSubtitle.setHintTextColor(item.editHintColor)
        }

        holder.itemView.tag = item

        holder.etWidgetItemSubtitle.tag = item
        holder.etWidgetItemSubtitle.setText(item.content)
        holder.etWidgetItemSubtitle.hint = item.hint

        if (item.subtitleSize > 0) {
            holder.etWidgetItemSubtitle.textSize = item.subtitleSize.toFloat()
        }

        if (item.editable) {
            holder.etWidgetItemSubtitle.inputType = item.inputType
            holder.etWidgetItemSubtitle.isFocusable = true
            holder.etWidgetItemSubtitle.isFocusableInTouchMode = true
        } else {
            holder.etWidgetItemSubtitle.inputType != InputType.TYPE_NULL
            holder.etWidgetItemSubtitle.isFocusable = false
            holder.etWidgetItemSubtitle.isFocusableInTouchMode = false
        }

        holder.etWidgetItemSubtitle.maxLines = 2
    }
}

class AvatarItemViewBinder(private val baseConfig: ItemConfiguration) : SimpleItemViewBinder<AvatarItem>() {

    override fun provideLayout(inflater: LayoutInflater, parent: ViewGroup) = R.layout.widget_items_item_avatar

    override fun onViewHolderCreated(viewHolder: KtViewHolder) {
        super.onViewHolderCreated(viewHolder)
        viewHolder.itemView.setPadding(
                baseConfig.itemStartPadding,
                baseConfig.itemTopPadding,
                baseConfig.itemEndPadding,
                baseConfig.itemBottomPadding
        )
    }

    override fun onBindViewHolder(holder: KtViewHolder, item: AvatarItem) {
        holder.ivWidgetAvatarItemArrow.visibleOrGone(item.hasArrow)
        holder.tvWidgetAvatarItemTitle.text = item.title

        val layoutParams = holder.ivWidgetItemAvatar.layoutParams

        if (layoutParams.height != item.avatarSize || layoutParams.width != item.avatarSize) {
            layoutParams.width = item.avatarSize
            layoutParams.height = item.avatarSize
            holder.ivWidgetItemAvatar.layoutParams = layoutParams
        }

        val showAvatar = item.showAvatar
        val source = item.source
        if (showAvatar != null) {
            showAvatar.invoke(holder.ivWidgetItemAvatar)
        } else if (source != null) {
            ImageLoaderFactory.getImageLoader().display(holder.ivWidgetItemAvatar, source)
        }

    }

}

class SwitchItemViewBinder(private val baseConfig: ItemConfiguration) : SimpleItemViewBinder<SwitchItem>() {

    var onItemSwitched: ((SwitchItem, Boolean) -> Unit)? = null

    private val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        val switchItem = (buttonView.tag as? SwitchItem) ?: return@OnCheckedChangeListener
        val itemSwitchedHandler = onItemSwitched ?: return@OnCheckedChangeListener
        (buttonView as SwitchButton).setCheckedImmediatelyNoEvent(switchItem.isOpen)
        itemSwitchedHandler.invoke(switchItem, isChecked)
    }

    override fun provideLayout(inflater: LayoutInflater, parent: ViewGroup) = R.layout.widget_items_switch

    override fun onViewHolderCreated(viewHolder: KtViewHolder) {
        super.onViewHolderCreated(viewHolder)
        viewHolder.itemView.setPadding(
                baseConfig.itemStartPadding,
                baseConfig.itemTopPadding,
                baseConfig.itemEndPadding,
                baseConfig.itemBottomPadding
        )
    }

    override fun onBindViewHolder(holder: KtViewHolder, item: SwitchItem) {
        holder.tvWidgetItemSwTitle.text = item.title

        holder.sbWidgetItemSwitch.setCheckedImmediatelyNoEvent(item.isOpen)
        holder.sbWidgetItemSwitch.tag = item
        holder.sbWidgetItemSwitch.setOnCheckedChangeListener(onCheckedChangeListener)
    }

}

private class EntranceItemDecoration(private val itemConfiguration: ItemConfiguration) : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.strokeWidth = itemConfiguration.cuttingLineWidth
        paint.color = itemConfiguration.cuttingLineColor
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val childAdapterPosition = parent.getChildAdapterPosition(view)

        @Suppress("UNCHECKED_CAST")
        val adapter = parent.adapter as DataManager<BaseItem>
        val item: BaseItem? = adapter.getItem(childAdapterPosition)
        if (item != null) {
            outRect.top = item.topMargin
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        @Suppress("UNCHECKED_CAST")
        val adapter = parent.adapter as DataManager<BaseItem>
        val childCount = parent.childCount
        var childView: View
        var textItem: BaseItem?

        for (index in 0 until childCount) {
            childView = parent.getChildAt(index)
            textItem = adapter.getItem(parent.getChildAdapterPosition(childView))

            if (textItem == null) {
                continue
            }

            if (textItem.hasTopCuttingLine) {
                if (itemConfiguration.paddingLine) {
                    c.drawLine(childView.left.toFloat() + itemConfiguration.itemStartPadding, childView.top.toFloat(), childView.right.toFloat() - itemConfiguration.itemEndPadding, childView.top.toFloat(), paint)
                } else {
                    c.drawLine(childView.left.toFloat(), childView.top.toFloat(), childView.right.toFloat(), childView.top.toFloat(), paint)
                }
            }
            if (textItem.hasBottomCuttingLine) {
                if (itemConfiguration.paddingLine) {
                    c.drawLine(childView.left.toFloat() + itemConfiguration.itemStartPadding, childView.bottom.toFloat(), childView.right.toFloat() - +itemConfiguration.itemEndPadding, childView.bottom.toFloat(), paint)
                } else {
                    c.drawLine(childView.left.toFloat(), childView.bottom.toFloat(), childView.right.toFloat(), childView.bottom.toFloat(), paint)
                }
            }

        }
    }

}

/**for handling crash when using EditableTextItem*/
class FixRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.dispatchTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}