package com.app.base.config

import com.android.base.utils.BaseUtils
import com.android.base.utils.android.storage.SpCache
import com.android.base.utils.common.ifNull

internal interface EnvironmentEditor {
    fun add(category: String, env: EnvironmentItem)
}

internal class EnvironmentItem(val name: String, val tag: String, val value: String)

internal object EnvironmentContext {

    private val spCache = SpCache(BaseUtils.getAppContext(), "app-host-storage-v2")

    private val envMap = LinkedHashMap<String, MutableList<EnvironmentItem>>()

    fun startEdit(adder: EnvironmentEditor.() -> Unit) {
        // do editing
        adder(object : EnvironmentEditor {
            override fun add(category: String, env: EnvironmentItem) {
                envMap.getOrPut(category) { mutableListOf() }.add(env)
            }
        })
        // set default value
        envMap.forEach { (category, list) ->
            val tag = spCache.getString(category, "")
            list.find { tag == it.tag }.ifNull {
                list.getOrNull(0)?.let {
                    select(category, it)
                } ?: throw NullPointerException("No env values provided.")
            }
        }
    }

    internal fun allCategory(): Map<String, List<EnvironmentItem>> {
        return envMap
    }

    internal fun select(category: String, env: EnvironmentItem) {
        spCache.putString(category, env.tag)
    }

    /**修改所选择的环境*/
    internal fun select(category: String, envTag: String) {
        envMap[category]?.find {
            envTag == it.tag
        }?.let {
            select(category, it)
        }
    }

    /**获得对应category的环境，默认返回第一个。*/
    internal fun selected(category: String): EnvironmentItem {
        val tag = spCache.getString(category, "")
        return envMap[category]?.find {
            tag == it.tag
        } ?: throw NullPointerException("no selected Environment with category: $category")
    }

}