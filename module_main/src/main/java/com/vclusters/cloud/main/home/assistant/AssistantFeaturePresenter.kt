package com.vclusters.cloud.main.home.assistant

import android.os.Bundle
import com.app.base.services.devicemanager.CloudDevice
import com.vclusters.cloud.main.databinding.MainFragmentAssistantBinding

typealias FeatureSwitchPredicate = (phoneId: Int, featureId: String) -> Boolean

class AssistantFeaturePresenter(
    private val featureUIAdapter: AssistantFeatureAdapter,
    private val vb: MainFragmentAssistantBinding,
    private val featureSwitchPredicate: FeatureSwitchPredicate
) {

    init {
        vb.mainFlChosePhone.setOnClickListener {

        }
    }

    private var selectedPhoneId = 0

    private var devices = emptyList<CloudDevice>()
    private var features = emptyList<AssistantFeature>()

    fun showDevices(devices: List<CloudDevice>) {
        if (this.devices == devices) {
            return
        }
        this.devices = devices
        val device = devices.find { selectedPhoneId == it.id } ?: devices[0]
        selectedPhoneId = device.id
        vb.mainTvPhoneName.text = device.diskName

        showFeatureChecked()
    }

    fun showFeatures(features: List<AssistantFeature>) {
        this.features = features
        showFeatureChecked()
    }

    private fun showFeatureChecked() {

    }

    fun recoverStateIfNeed(savedInstanceState: Bundle?) {

    }

    fun saveInstanceState(outState: Bundle) {

    }

}