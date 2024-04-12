package me.ztiany.wan.account.data

data class LoginRequest(
    val phoneNumber: String,
    val type: String,
    val code: String,
    val devicesId: String,
    val diskName: String,
    val versionNumber: Int,
    /** 固定值 */
    val clientResouce: Int = 10,
)

data class CodeRequest(

    val phone: String,

    /**
     * 验证码类型：
     *
     *   - 注册：REGISTER ;
     *   - 验证码登陆：CODELOGIN ;
     *   - 重置密码：RESETPWD ;
     *   - 注销：CANCELLATION。
     */
    val type: String
) {
    companion object {
        const val TYPE_LOGIN = "CODELOGIN"
    }
}