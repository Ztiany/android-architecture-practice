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
     * Capture an image through the system camera.
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.TakePicture]
     * @see [androidx.activity.result.contract.ActivityResultContracts.CaptureVideo]
     */
    fun captureImage(): ImageCapturer

    /**
     * Capture a video through the system camera.
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.TakePicture]
     * @see [androidx.activity.result.contract.ActivityResultContracts.CaptureVideo]
     */
    fun captureVideo(): VideoCapturer

    /**
     * Get videos through the system selector. The Intent.ACTION_GET_CONTENT is used.
     * If you are confused about the difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT, Maybe you need to refers to:
     * [What is the real difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT?](https://stackoverflow.com/questions/36182134/what-is-the-real-difference-between-action-get-content-and-action-open-document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetContent]
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents]
     */
    fun getContent(): GetContent

    /**
     * Get videos through the system selector. The Intent.ACTION_GET_CONTENT is used.
     * If you are confused about the difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT, Maybe you need to refers to:
     * [What is the real difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT?](https://stackoverflow.com/questions/36182134/what-is-the-real-difference-between-action-get-content-and-action-open-document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetContent]
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents]
     */
    fun getImageContent(): GetImageContent

    /**
     * Get videos through the system selector. The Intent.ACTION_GET_CONTENT is used.
     * If you are confused about the difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT, Maybe you need to refers to:
     * [What is the real difference between ACTION_GET_CONTENT and ACTION_OPEN_DOCUMENT?](https://stackoverflow.com/questions/36182134/what-is-the-real-difference-between-action-get-content-and-action-open-document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetContent]
     * @see [androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents]
     */
    fun getVideoContent(): GetVideoContent

    /**
     * Get photos through the PhotoPicker or SAF. default is PhotoPicker.
     *
     * refers to the following links for more details:
     *
     * - [Photo Picker](https://developer.android.com/training/data-storage/shared/photopicker).
     * - [Open files using the Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
     * - [Access documents and other files from shared storage](https://developer.android.com/training/data-storage/shared/documents-files)
     * - [Document: ACTION_OPEN_DOCUMENT](https://developer.android.com/reference/kotlin/android/content/Intent#action_open_document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenDocument]
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia]
     */
    fun pickImage(): ImagePicker

    /**
     * Get photos through the PhotoPicker or SAF. default is PhotoPicker.
     *
     * refers to the following links for more details:
     *
     * - [Photo Picker](https://developer.android.com/training/data-storage/shared/photopicker).
     * - [Open files using the Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
     * - [Access documents and other files from shared storage](https://developer.android.com/training/data-storage/shared/documents-files)
     * - [Document: ACTION_OPEN_DOCUMENT](https://developer.android.com/reference/kotlin/android/content/Intent#action_open_document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenDocument]
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia]
     */
    fun pickVideo(): VideoPicker

    /**
     * Get photos through the PhotoPicker or SAF. default is PhotoPicker.
     *
     * refers to the following links for more details:
     *
     * - [Photo Picker](https://developer.android.com/training/data-storage/shared/photopicker).
     * - [Open files using the Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
     * - [Access documents and other files from shared storage](https://developer.android.com/training/data-storage/shared/documents-files)
     * - [Document: ACTION_OPEN_DOCUMENT](https://developer.android.com/reference/kotlin/android/content/Intent#action_open_document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenDocument]
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia]
     */
    fun pickImageAndVideo(): ImageAndVideoPicker

    /**
     * Get files through the SAF.
     *
     * refers to the following links for more details:
     *
     * - [Open files using the Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
     * - [Access documents and other files from shared storage](https://developer.android.com/training/data-storage/shared/documents-files)
     * - [Document: ACTION_OPEN_DOCUMENT](https://developer.android.com/reference/kotlin/android/content/Intent#action_open_document)
     *
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenDocument]
     * @see [androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia]
     * @see [androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia]
     */
    fun pickFile(): FilePicker

    /**
     * Get photos by MediaStore API. refers to [Access media files from shared storage](https://developer.android.com/training/data-storage/shared/media) for more details.
     */
    fun selectImage(): SelectImageAction

    /**
     * Get videos by MediaStore API. refers to [Access media files from shared storage](https://developer.android.com/training/data-storage/shared/media) for more details.
     */
    fun selectVideo(): SelectVideoAction

}