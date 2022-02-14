package com.vclusters.cloud.main.home.phone.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.vclusters.cloud.main.databinding.MainWidgetAnnouncementBinding

private const val INTERVAL = 5000L

class AnnouncementView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val vb = MainWidgetAnnouncementBinding.inflate(LayoutInflater.from(context), this)

    private var announcements = emptyList<String>()
    private var currentIndex = 0

    private val switchAction = object : Runnable {
        override fun run() {
            showCurrentAnnouncement(announcements[currentIndex])
            postDelayed(this, INTERVAL)
            currentIndex++
            if (currentIndex > announcements.size - 1) {
                currentIndex = 0
            }
        }
    }

    private fun showCurrentAnnouncement(text: String) {
        vb.mainTvAnnouncement.text = text
    }

    fun setAnnouncements(announcements: List<String>) {
        this.announcements = announcements
        removeCallbacks(switchAction)
        if (announcements.isEmpty()) {
            showCurrentAnnouncement("暂无公告")
        } else {
            post(switchAction)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAnnouncements(announcements)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(switchAction)
    }

}