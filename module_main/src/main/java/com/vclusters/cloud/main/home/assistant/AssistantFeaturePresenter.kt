package com.vclusters.cloud.main.home.assistant

import android.app.Activity
import android.os.Bundle
import com.android.base.utils.android.views.realContext
import com.app.base.services.devicemanager.CloudDevice
import com.app.base.widget.dialog.showBottomSheetListDialog
import com.vclusters.cloud.main.databinding.MainFragmentAssistantBinding

typealias FeatureSwitchPredicate = (phoneId: Int, featureId: String) -> Boolean

class AssistantFeaturePresenter(
    private val featureUIAdapter: AssistantFeatureAdapter,
    private val vb: MainFragmentAssistantBinding,
    private val featureSwitchPredicate: FeatureSwitchPredicate
) {

    init {
        vb.mainFlChosePhone.setOnClickListener { view ->
            (view.realContext as? Activity)?.showBottomSheetListDialog {
                items = devices.map { it.diskName }
                selectedPosition = devices.indexOfFirst { it.id == selectedPhoneId }
                itemSelectedListener = { position, _ ->
                    selectedPhoneId = devices[position].id
                    vb.mainTvPhoneName.text = devices[position].diskName
                }
            }
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
        if (features.isEmpty() || devices.isEmpty()) {
            return
        }
        features.map {
            if (it.hasSwitch) {
                it.copy(switchStateOn = featureSwitchPredicate(selectedPhoneId, it.flag))
            } else {
                it
            }
        }.let {
            featureUIAdapter.replaceAll(it)
        }
    }

    fun recoverStateIfNeed(savedInstanceState: Bundle?) {

    }

    fun saveInstanceState(outState: Bundle) {

    }

}