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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val IS_FILLED_HISTORY_PASSWORD = "IS_FILLED_HISTORY_PASSWORD"

@AndroidEntryPoint
class LoginFragment : BaseUIFragment<AccountFragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private var isFilledHistoryPassword = false

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
        viewBinding.accountEtPhone.addTextChangedListener {
            ensureIfLoginBtnEnable()
        }

        viewBinding.accountEtPhone.setOnTailingIconClickListener { iconsEditText, pendingState ->
            if (pendingState) {
                showHistoryUserWindow(iconsEditText)
            }
        }

        viewBinding.accountEtPassword.addTextChangedListener {
            if (it.toString().isEmpty()) {
                isFilledHistoryPassword = false
            }
            ensureIfLoginBtnEnable()
        }

        viewBinding.accountEtPassword.setOnShowPasswordListener {
            if (isFilledHistoryPassword) {
                it.setText("")
            }
        }

        viewBinding.accountBtnLogin.setOnClickListener {
            loginChecked()
        }
    }

    private fun showHistoryUserWindow(iconsEditText: IconsEditText) {
        val historyUsers = viewModel.historyUsers
        Timber.d(historyUsers.toString())
        if (historyUsers.isEmpty()) {
            return
        }
        iconsEditText.isTailingIconStateOn = true
        showHistoryUserPopupWindow(requireContext(), viewBinding.accountEtPhone, historyUsers,
            onSelected = {
                viewBinding.accountEtPhone.setText(it.phone)
                if (!viewBinding.accountEtPassword.isPasswordSeeable) {
                    viewBinding.accountEtPhone.setText(it.password)
                    isFilledHistoryPassword = true
                }
            },
            onDeleted = {
                viewModel.deleteHistoryUser(it)
            },
            onDismiss = {
                lifecycleScope.launch {
                    delay(100)
                    iconsEditText.isTailingIconStateOn = false
                }
            }
        )
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

        viewModel.historyUserEnable.observe(this) {
            viewBinding.accountEtPhone.setTailingIconEnable(it)
        }
    }

}