package com.vclusters.cloud.account.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.handleFlowData
import com.android.base.utils.android.views.textValue
import com.app.base.widget.form.validateCellphone
import com.app.base.widget.form.validatePassword
import com.vclusters.cloud.account.AccountNavigator
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.databinding.AccountFragmentLoginBinding
import com.vclusters.cloud.account.widget.IconsEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseUIFragment<AccountFragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject lateinit var accountNavigator: AccountNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        viewBinding.accountEtPassword.addTextChangedListener {
            ensureIfLoginBtnEnable()
        }

        viewBinding.accountEtPassword.addTextChangedListener {
            ensureIfLoginBtnEnable()
        }

        viewBinding.accountBtnLogin.setOnClickListener {
            loginChecked()
        }

        viewBinding.accountEtPhone.setOnTailingIconClickListener { iconsEditText, pendingState ->
            if (pendingState) {
                showHistoryUserWindow(iconsEditText)
            } else {
                dismissHistoryUserWindow()
            }
        }

        viewBinding.accountEtPhone.setText("19999999999")
        viewBinding.accountEtPassword.setText("123456789")
    }

    private fun dismissHistoryUserWindow() {

    }

    private fun showHistoryUserWindow(iconsEditText: IconsEditText) {
        val historyUsers = viewModel.historyUsers
        iconsEditText.setTailingIconState(true)
        if (historyUsers.isEmpty()) {
            return
        }
        showHistoryUserPopupWindow(requireContext(), viewBinding.accountEtPhone, historyUsers)
    }

    private fun ensureIfLoginBtnEnable() {
        viewBinding.accountBtnLogin.isEnabled =
            viewBinding.accountEtPassword.textValue().isNotEmpty() && viewBinding.accountEtPhone.textValue().isNotEmpty()
    }

    private fun loginChecked() {
        if (!validateCellphone(viewBinding.accountEtPhone)) {
            return
        }
        if (!validatePassword(viewBinding.accountEtPassword)) {
            return
        }
        viewModel.login(viewBinding.accountEtPhone.textValue(), viewBinding.accountEtPassword.textValue())
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            handleFlowData(viewModel.loginState) {
                onSuccess = {
                    showMessage(R.string.login_success)
                    accountNavigator.exitAndToHomePage()
                }
            }
        }
    }

}