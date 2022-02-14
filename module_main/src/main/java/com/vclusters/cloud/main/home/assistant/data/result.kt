package com.vclusters.cloud.main.home.assistant.data


data class AssistantFeatureConfig(
    val id: Int = 0,
    val enableFlag: Int = 0,
    val extendKey: String = "",
    val extendName: String = "",
    val openStatus: Int = 0,
    val updateBy: String = "",
    val updateTime: String = ""
)