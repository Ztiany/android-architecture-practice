package com.vclusters.cloud.account.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.fragment.tools.exitFragment
import com.android.base.architecture.ui.handleLiveData
import com.android.base.utils.android.argument
import com.android.base.utils.android.views.textValue
import com.android.base.utils.android.views.visible
import com.app.base.widget.form.validateCellphone
import com.app.base.widget.form.validatePassword
import com.vclusters.cloud.account.AccountNavigator
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.databinding.AccountFragmentLoginBinding
import com.vclusters.cloud.account.widget.IconsEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 登录/添加账号 界面 */
@AndroidEntryPoint
class LoginFragment : BaseUIFragment<AccountFragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private var lastInputPhone = ""

    private var isFilledHistoryPassword = false

    private val isAddAccount by argument(ADD_ACCOUNT_KEY, false)

    @Inject lateinit var accountNavigator: AccountNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpViews()
        savedInstanceState?.let {
            isFilledHistoryPassword = it.getBoolean(IS_FILLED_HISTORY_PASSWORD, false)
        }
    }

    private fun setUpViews() {
        vb.accountEtPhone.addTextChangedListener {
            ensureIfLoginBtnEnable()
            if (!isAddAccount && lastInputPhone.isEmpty() && it.toString().isNotEmpty()) {
                showHistoryUserWindow(vb.accountEtPhone)
            }
            lastInputPhone = it.toString()
        }

        vb.accountEtPhone.setOnTailingIconClickListener { iconsEditText, pendingState ->
            if (pendingState) {
                showHistoryUserWindow(iconsEditText)
            }
        }

        vb.accountEtPassword.addTextChangedListener {
            if (it.toString().isEmpty()) {
                isFilledHistoryPassword = false
            }
            ensureIfLoginBtnEnable()
        }

        vb.accountEtPassword.setOnShowPasswordListener {
            if (isFilledHistoryPassword) {
                it.setText("")
            }
        }

        vb.accountBtnLogin.setOnClickListener {
            loginChecked()
        }

        if (isAddAccount) {
            vb.accountTvCancelAdd.visible()
            vb.accountTvCancelAdd.setOnClickListener {
                exitFragment()
            }
        } else {
            vb.accountFlSettings.visible()
            vb.accountFlSettings.setOnClickListener {
                showMessage("TODO")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_FILLED_HISTORY_PASSWORD, isFilledHistoryPassword)
    }

    private fun showHistoryUserWindow(iconsEditText: IconsEditText) {
        val historyUsers = viewModel.historyUsers
        if (historyUsers.isEmpty()) {
            return
        }
        iconsEditText.isTailingIconStateOn = true
        showHistoryUserPopupWindow(requireContext(), vb.accountEtPhone, historyUsers,
            onSelected = {
                lastInputPhone = it.phone//just for preventing popup window from showing.
                vb.accountEtPhone.setText(it.phone)
                vb.accountEtPassword.hidePassword()
                vb.accountEtPassword.setText(it.password)
                isFilledHistoryPassword = true
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
        vb.accountBtnLogin.isEnabled =
            vb.accountEtPassword.textValue().isNotEmpty() && vb.accountEtPhone.textValue().isNotEmpty()
    }

    private fun loginChecked() {
        if (!validateCellphone(vb.accountEtPhone)) {
            return
        }
        if (!validatePassword(vb.accountEtPassword)) {
            return
        }
        viewModel.login(vb.accountEtPhone.textValue(), vb.accountEtPassword.textValue())
    }

    private fun subscribeViewModel() {
        handleLiveData(viewModel.loginState) {
            onSuccess = {
                showMessage(R.string.login_success)
                accountNavigator.exitAndToHomePage()
            }
        }

        viewModel.historyUserEnable.observe(this) {
            vb.accountEtPhone.setTailingIconEnable(it && !isAddAccount)
        }
    }

    companion object {

        private const val IS_FILLED_HISTORY_PASSWORD = "is_filled_history_password"
        private const val ADD_ACCOUNT_KEY = "add_account_key"

        fun newLoginFragment(addAccount: Boolean): Fragment {
            return LoginFragment().also {
                it.arguments = bundleOf(
                    ADD_ACCOUNT_KEY to addAccount
                )
            }
        }

    }

}