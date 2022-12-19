package com.biyun.cg.box.account.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.handleLiveData
import com.android.base.utils.android.views.textValue
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.account.AccountInternalNavigator
import com.biyun.cg.box.account.R
import com.biyun.cg.box.account.databinding.AccountFragmentLoginBinding
import javax.inject.Inject

/** 登录/添加账号 界面 */
@AndroidEntryPoint
class LoginFragment : BaseUIFragment<AccountFragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject lateinit var accountInternalNavigator: AccountInternalNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        vb.accountBtnLogin.setOnClickListener {
            viewModel.login(vb.accountEtPhone.textValue(), vb.accountEtPassword.textValue())
        }
    }

    private fun subscribeViewModel() {
        handleLiveData(viewModel.loginState) {
            onSuccess = {
                showMessage(R.string.login_success)
                accountInternalNavigator.exitAndToHomePage()
            }
        }
    }

}