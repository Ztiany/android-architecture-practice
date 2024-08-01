package com.android.sdk.mediaselector

import com.android.sdk.mediaselector.actions.FilePicker
import com.android.sdk.mediaselector.actions.GetContent
import com.android.sdk.mediaselector.actions.GetImageContent
import com.android.sdk.mediaselector.actions.GetVideoContent
import com.android.sdk.mediaselector.actions.ImageAndVideoPicker
import com.android.sdk.mediaselector.actions.ImageCapturer
import com.android.sdk.mediaselector.actions.ImagePicker
import com.android.sdk.mediaselector.actions.VideoCapturer
import com.android.sdk.mediaselector.actions.VideoPicker
import com.android.sdk.mediaselector.actions.SelectImageAction
import com.android.sdk.mediaselector.actions.SelectVideoAction
import com.android.sdk.mediaselector.processor.Processor

interface MediaSelector : ComponentStateHandler {

    /**
     * Get photos and files through the system camera, PhotoPicker or SAF.
     */
    fun captureImage(): ImageCapturer

    fun captureVideo(): VideoCapturer

    fun getContent(): GetContent

    fun getImageContent(): GetImageContent

    fun getVideoContent(): GetVideoContent

    fun pickImage(): ImagePicker

    fun pickVideo(): VideoPicker

    fun pickImageAndVideo(): ImageAndVideoPicker

    fun pickFile(): FilePicker

    /**
     * Get photos by MediaStore API.
     */
    fun selectImage(): SelectImageAction

    /**
     * Get photos by MediaStore API.
     */
    fun selectVideo(): SelectVideoAction

    fun withPostProcessor(vararg processors: Processor): MediaSelector

}