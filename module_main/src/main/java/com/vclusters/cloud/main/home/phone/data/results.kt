package com.vclusters.cloud.main.home.phone.data

data class HomeAnnouncements(
    val list: List<HomeAnnouncement> = emptyList()
)

data class HomeAnnouncement(
    val title: String = ""
)