package com.vclusters.cloud.main.home.assistant

data class AssistantFeature(
    val flag: String,
    val icon: Int,
    val name: String,
    val nameIcon: Int = 0,
    val desc: String = "",
    val hasArrow: Boolean = true,
    val hasSwitch: Boolean = false,
    val switchStateOn: Boolean = false
)