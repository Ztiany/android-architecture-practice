val libs = listOf(
    "lib_foundation",
    "lib_base",
    "lib_network",
    "lib_cache",
    "lib_common_ui",
    "lib_permission",
    "lib_media_selector",
    "lib_upgrade",
    "lib_social",
    "lib_safekeyboard",
    "lib_biometrics",
    "lib_qrcode",
    "lib_image_loader",
    "lib_rxjava2"
)

libs.forEach {
    include(":$it")
    project(":$it").projectDir = File("./libraries/$it")
}

//业务基础架构
include(":common_spec")
include(":common_ui")
include(":common_sdk")

//业务模块
include(":module_main")
include(":module_main_api")

include(":module_account")
include(":module_account_api")

//app壳
include(":app")

