#include <jni.h>
#include <string.h>
#include <android/log.h>

#define LOG_TAG "C-Log"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/*这是APP的正式签名*/
static char *SIGN = "308203573082023fa003020102020425ec5feb300d06092a864886f70d01010b0500305c310b"
        "3009060355040613023836310b3009060355040813026764310b300906035504071302737a3110300e060355"
        "040a1307616e64726f69643110300e060355040b1307616e64726f6964310f300d060355040313065a7469616e7"
        "9301e170d3137303831373032343534315a170d3432303831313032343534315a305c310b300906035504061302"
        "3836310b3009060355040813026764310b300906035504071302737a3110300e060355040a1307616e64726f696"
        "43110300e060355040b1307616e64726f6964310f300d060355040313065a7469616e7930820122300d06092a8648"
        "86f70d01010105000382010f003082010a02820101008318b394b4fda1ff22ca0ae576e3b41f52440fcc2dc5f769c0ce"
        "a14ded25c9be3821726b279a11b61a54a403ce973a1cd68d01d138660b032858697997cfb58a202069414211f5e310fd2"
        "83b634d5263f83b4fae4064ee00075aa0b251cce5bb3bdad4139a1dc96b2c7f9a66f23c6d87ae44de27264ccd2bdfcd68"
        "4ab0bba70918d59b4ef1d1b69f95d4d44b39b03d098d8b9e71f1b35f5225931fde9de7593acd699313225490c6378a45"
        "f8f281c373555a119e0f77d09975e24c3286558665d68be9fd884ba74704a1a3d12b40e0489057134ccf0dfe3221a97a27"
        "8ed8fbda2a5b5e1bf963846673cecf66aeb8d5fa4f53857633380bc092995c874f0df4f90203010001a321301f301d06035"
        "51d0e041604141cec077920f2bf172dceab631f734f5c2f3f7ff3300d06092a864886f70d01010b050003820101003be44ee6"
        "cb5f0e1fe21eb373406c5eba59fe162beba44836896d56e96c67b2d44c34eaa22889d10899b99528611878145675c9db7906"
        "39d84bc7f9d438e42df525f5fa8cef758234bc89377ce83c6e01778d0bf675d77c4cb44f1cde6b59e06201deff52a27c2b0020"
        "c9c1edcfe34cf86f14690c951f5e15d99706819c9c173814d8311126bea573bb337e3be9b007099c6d762d3ec6188dd7e7fea479"
        "2757a38989b4cdec951c1b3a4889c4665cd3d726e0ced2bcc58c7591567d2bc8664a21617fcf595c4feaaacb56a2053d8e745b"
        "23fc90b6dcaf285e85d7c8349fba3a23842e53a56c75798913fa41d6799592807fc53c63c341f0463494665e8efd9aa3";

static jint GET_SIGNATURES = 64;


/**
 * 获取java层的Application对象
 */
static jobject getApplication(JNIEnv *env) {

    jobject application = NULL;
    jclass activity_thread_clz = (*env)->FindClass(env, "android/app/ActivityThread");

    if (activity_thread_clz != NULL) {

        jmethodID currentApplication = (*env)->GetStaticMethodID(
                env,
                activity_thread_clz,
                "currentApplication",
                "()Landroid/app/Application;");

        if (currentApplication != NULL) {
            application = (*env)->CallStaticObjectMethod(
                    env,
                    activity_thread_clz,
                    currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        (*env)->DeleteLocalRef(env, activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }
    return application;
}


static int verifySign(JNIEnv *env) {
    // Application object
    jobject application = getApplication(env);
    if (application == NULL) {
        return JNI_ERR;
    }
    // Context(ContextWrapper) class
    jclass context_clz = (*env)->GetObjectClass(env, application);
    // getPackageManager()
    jmethodID getPackageManager = (*env)->GetMethodID(
            env,
            context_clz,
            "getPackageManager",
            "()Landroid/content/pm/PackageManager;");

    // android.content.pm.PackageManager object
    jobject package_manager = (*env)->CallObjectMethod(env, application, getPackageManager);
    // PackageManager class
    jclass package_manager_clz = (*env)->GetObjectClass(env, package_manager);
    // getPackageInfo()
    jmethodID getPackageInfo = (*env)->GetMethodID(env,
                                                   package_manager_clz,
                                                   "getPackageInfo",
                                                   "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // context.getPackageName()
    jmethodID getPackageName = (*env)->GetMethodID(env,
                                                   context_clz,
                                                   "getPackageName",
                                                   "()Ljava/lang/String;");

    // call getPackageName() and cast from jobject to jstring
    jstring package_name = (jstring) ((*env)->CallObjectMethod(env, application, getPackageName));

    // PackageInfo object
    jobject package_info = (*env)->CallObjectMethod(
            env,
            package_manager,
            getPackageInfo,
            package_name,
            GET_SIGNATURES);

    // class PackageInfo
    jclass package_info_clz = (*env)->GetObjectClass(env, package_info);

    // field signatures
    jfieldID signatures_field = (*env)->GetFieldID(
            env,
            package_info_clz,
            "signatures",
            "[Landroid/content/pm/Signature;");

    jobject signatures = (*env)->GetObjectField(env, package_info, signatures_field);
    jobjectArray signatures_array = (jobjectArray) signatures;
    jobject signature0 = (*env)->GetObjectArrayElement(env, signatures_array, 0);
    jclass signature_clz = (*env)->GetObjectClass(env, signature0);

    jmethodID toCharsString = (*env)->GetMethodID(
            env,
            signature_clz,
            "toCharsString",
            "()Ljava/lang/String;");

    // call toCharsString()
    jstring signature_str = (jstring) (*env)->CallObjectMethod(env, signature0, toCharsString);

    // release
    (*env)->DeleteLocalRef(env, application);
    (*env)->DeleteLocalRef(env, context_clz);
    (*env)->DeleteLocalRef(env, package_manager);
    (*env)->DeleteLocalRef(env, package_manager_clz);
    (*env)->DeleteLocalRef(env, package_name);
    (*env)->DeleteLocalRef(env, package_info);
    (*env)->DeleteLocalRef(env, package_info_clz);
    (*env)->DeleteLocalRef(env, signatures);
    (*env)->DeleteLocalRef(env, signature0);
    (*env)->DeleteLocalRef(env, signature_clz);

    const char *sign = (*env)->GetStringUTFChars(env, signature_str, NULL);
    if (sign == NULL) {
        LOGE("分配内存失败");
        return JNI_ERR;
    }

    LOGI("应用中读取到的签名首字母为：%c", *sign);
    //LOGI("应用中读取到的签名为：%s", sign);
    LOGI("native中预置的签名首字母为：%c", *SIGN);
    //LOGI("native中预置的签名为：%s", SIGN);

    int result = strcmp(sign, SIGN);
    // 使用之后要释放这段内存
    (*env)->ReleaseStringUTFChars(env, signature_str, sign);
    (*env)->DeleteLocalRef(env, signature_str);
    // 签名一致
    if (result == 0) {
        return JNI_OK;
    }
    return JNI_ERR;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("验证APP签名");
    JNIEnv *env = NULL;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }
    if (verifySign(env) == JNI_OK) {
        LOGI("APP签名验证通过");
        return JNI_VERSION_1_4;
    }
    LOGE("APP签名不一致!");
    return JNI_ERR;
}