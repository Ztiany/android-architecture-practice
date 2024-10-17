package me.ztiany.wan.account.data

internal data class CodeRequest(

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