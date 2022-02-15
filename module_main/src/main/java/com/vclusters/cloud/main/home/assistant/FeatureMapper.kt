package com.vclusters.cloud.main.home.assistant

import android.content.Context
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.home.assistant.data.AssistantFeatureConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal const val FEATURE_PHOTO = "photograph"
internal const val FEATURE_VIDEO = "takeAVideo"
internal const val FEATURE_RECORD = "europeanCommission"
internal const val FEATURE_GPS = "gpsPierceThrough"
internal const val FEATURE_SCREEN = "screenAdaptation"
internal const val FEATURE_IMAGE = "image"
internal const val FEATURE_CONFIG = "cardConfig"

private typealias FeatureFactory = (config: AssistantFeatureConfig, context: Context) -> AssistantFeature

private val featureFactories: Map<String, FeatureFactory> = mapOf(
    FEATURE_PHOTO to { config, context ->
        AssistantFeature(FEATURE_PHOTO, R.drawable.main_icon_assistant_photo, config.extendName, desc = "图片可在云手机“文件管理器”的“拍照”内查看")
    },

    FEATURE_VIDEO to { config, context ->
        AssistantFeature(FEATURE_VIDEO, R.drawable.main_icon_assistant_video, config.extendName, desc = "视频可在云手机“文件管理器”的“拍视频”内查看")
    },

    FEATURE_RECORD to { config, context ->
        AssistantFeature(FEATURE_RECORD, R.drawable.main_icon_assistant_record, config.extendName, desc = "音频可在云手机“文件管理器”的“录音”内查看")

    },

    FEATURE_GPS to { config, context ->
        AssistantFeature(FEATURE_GPS, R.drawable.main_icon_assistant_location, config.extendName, hasArrow = false, hasSwitch = true)
    },

    FEATURE_SCREEN to { config, context ->
        AssistantFeature(
            FEATURE_SCREEN,
            R.drawable.main_icon_assistant_screen,
            config.extendName,

            desc = "开启后，云手机将根据本机屏幕自适应分辨率",
            hasArrow = false,
            hasSwitch = true
        )
    },

    FEATURE_IMAGE to { config, context ->
        AssistantFeature(
            FEATURE_IMAGE,
            R.drawable.main_icon_assistant_image,
            config.extendName,
            desc = "开启后，云手机将切换至高清图像模式",
            nameIcon = R.drawable.icon_info,
            hasArrow = false,
            hasSwitch = true
        )
    },

    FEATURE_CONFIG to { config, context ->
        AssistantFeature(FEATURE_CONFIG, R.drawable.main_icon_assistant_config, config.extendName)
    },
)

class FeatureMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun mapperConfigs(configs: List<AssistantFeatureConfig>): List<AssistantFeature> {
        return configs.mapNotNull {
            featureFactories[it.extendKey]?.invoke(it, context)
        }
    }

}