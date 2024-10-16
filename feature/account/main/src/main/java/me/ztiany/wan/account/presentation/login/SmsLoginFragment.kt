package me.ztiany.wan.account.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.base.handleLiveData
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.enableSpanClickable
import com.android.base.utils.android.views.expandTouchArea
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.textValue
import com.app.base.utils.form.validateCellphone
import com.app.base.utils.form.validateSmsCode
import com.app.base.utils.newAppStyleClickSpan
import com.blankj.utilcode.util.SpanUtils
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.account.AccountInternalNavigator
import me.ztiany.wan.account.R
import me.ztiany.wan.account.databinding.AccountFragmentSmsBinding
import javax.inject.Inject

@AndroidEntryPoint
class SmsLoginFragment : BaseUIFragment<AccountFragmentSmsBinding>() {

    private val viewModel: SmsLoginViewModel by viewModels()

    @Inject internal lateinit var navigator: AccountInternalNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        super.onSetupCreatedView(view, savedInstanceState)
        setUpListeners()
        setUpAgreement()
    }

    private fun setUpAgreement() = withVB {
        accountTvAgreement.onThrottledClick {

        }
        accountTvAgreement.enableSpanClickable()
        accountTvAgreement.text = SpanUtils()
            .append("新用户登录即完成注册，且表示您已阅读并同意")
            .append("《用户使用协议》")
            .setClickSpan(newAppStyleClickSpan(this@SmsLoginFragment, requireContext()) {
                navigator.checkUserProtocol()
            })
            .append("、")
            .append("《隐私协议》")
            .setClickSpan(newAppStyleClickSpan(this@SmsLoginFragment, requireContext()) {
                navigator.checkPrivacyProtocol()
            })
            .append("。")
            .create()
    }

    private fun setUpListeners() = withVB {
        accountTvCounter.setOnClickListener {
            sendSmsCodeChecked()
        }

        accountBtnLogin.onThrottledClick {
            doLoginChecked()
        }

        accountViewCheckAgreement.setOnClickListener {
            vb.accountViewCheckAgreement.isSelected = !vb.accountViewCheckAgreement.isSelected
        }
        accountViewCheckAgreement.expandTouchArea(dip(15), dip(15))
    }

    private fun sendSmsCodeChecked() {
        if (validateCellphone(vb.accountEtPhone)) {
            viewModel.sendSmsCode(vb.accountEtPhone.textValue())
        }
    }

    private fun doLoginChecked() {
        if (validateCellphone(vb.accountEtPhone) && validateSmsCode(vb.accountEtSms) && checkAgreement()) {
            viewModel.smsLogin(vb.accountEtPhone.textValue(), vb.accountEtSms.textValue())
        }
    }

    private fun checkAgreement(): Boolean {
        if (!vb.accountViewCheckAgreement.isSelected) {
            showMessage(getString(R.string.account_agreement_tips))
        }
        return vb.accountViewCheckAgreement.isSelected
    }

    override fun onResume() {
        super.onResume()
        vb.accountViewCheckAgreement.isSelected = viewModel.agreeWithUserProtocol
    }

    private fun subscribeViewModel() {
        handleLiveData(viewModel.smsCodeState) {
            onSuccess {
                vb.accountTvCounter.startCounter()
                showMessage(com.app.base.ui.theme.R.string.validate_code_send_success)
            }
        }

        handleLiveData(viewModel.loginState) {
            onSuccess {
                showMessage(com.app.base.ui.theme.R.string.login_success)
                vb.accountTvCounter.clearCounterStateWhenDetach()
                navigator.exitAndToHomePage()
            }
        }
    }

}