package com.vclusters.cloud.account.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.vclusters.cloud.account.databinding.AccountFragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseUIFragment<AccountFragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)

    }

}