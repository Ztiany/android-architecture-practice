package me.ztiany.widget.clicks

import android.view.View

/**
 *@author Ztiany
 */
abstract class SingleClickListener(private val duration: Int = 300) : View.OnClickListener {

    private var time = System.currentTimeMillis()

    final override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (duration <= currentTimeMillis - time) {
            onSingleClick(v)
            time = System.currentTimeMillis()
        }
    }

    abstract fun onSingleClick(view: View)

}

fun View.setSingleClickListener(duration: Int = 300, listener: View.OnClickListener) {
    setOnClickListener(object : SingleClickListener(duration) {
        override fun onSingleClick(view: View) {
            listener.onClick(view)
        }
    })
}