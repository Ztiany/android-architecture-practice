package com.app.base.utils.form

import android.view.View
import android.widget.TextView
import com.android.base.utils.android.views.getString
import com.android.base.utils.android.views.textValue
import com.app.base.dialog.toast.ToastKit
import com.google.android.material.textfield.TextInputLayout

/** 验证手机号的输入 */
fun validateCellphone(view: View): Boolean {
    return CellphoneNumberValidator(view).validate()
}

/** 验证姓名 */
fun validateName(view: View): Boolean {
    return NameValidator(view).validate()
}

/** 验证邮箱 */
fun validateEmail(view: View): Boolean {
    return EmailValidator(view).validate()
}

/** 验证密码输入 */
fun validatePassword(view: View): Boolean {
    return PasswordValidator(view).validate()
}

/** 验证旧密码输入 */
fun validateOldPassword(view: View): Boolean {
    return OldPasswordValidator(view).validate()
}

/** 验证新密码输入 */
fun validateNewPassword(view: View): Boolean {
    return NewPasswordValidator(view).validate()
}

/** 验证确认密码 */
fun validateConfirmPassword(confirmPassword: String, view: View): Boolean {
    return ConfirmPasswordValidator(view, confirmPassword).validate()
}

/** 验证图像验证码 */
fun validateImageValidatingCode(view: View): Boolean {
    return ImageCodeValidator(view).validate()
}

/** 验证证件号（身份证）*/
fun validatePaperworkCode(view: View): Boolean {
    return IdentityCodeValidator(view).validate()
}

/** 验证验证码 */
fun validateSmsCode(view: View): Boolean {
    return CodeValidator(view).validate()
}


///////////////////////////////////////////////////////////////////////////
// utils
///////////////////////////////////////////////////////////////////////////
internal fun handleNoMatch(view: View, strId: Int) {
    val message = getString(strId)
    ToastKit.showMessage(view.context, message)
}

internal fun handleMatch(view: View) {

}

internal fun getStringData(view: View, trim: Boolean = false): String {
    return when (view) {
        is TextView -> if (trim) view.textValue().trim() else view.textValue()
        is TextInputLayout -> if (trim) view.textValue().trim() else view.textValue()
        else -> {
            throw IllegalArgumentException("ViewDataAdapter unSupport ")
        }
    }
}