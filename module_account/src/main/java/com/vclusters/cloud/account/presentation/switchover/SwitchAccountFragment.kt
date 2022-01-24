package com.vclusters.cloud.account.presentation.switchover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.adapter.newOnItemClickListener
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.handleFlowData
import com.vclusters.cloud.account.AccountNavigator
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.data.HistoryUser
import com.vclusters.cloud.account.databinding.AccountFragmentSwitchBinding
import com.vclusters.cloud.account.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SwitchAccountFragment : BaseUIFragment<AccountFragmentSwitchBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject lateinit var accountNavigator: AccountNavigator
    @Inject lateinit var mSwitchAccountAdapter: SwitchAccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)

        //adapter
        with(mSwitchAccountAdapter) {
            onItemSelectedListener = newOnItemClickListener<HistoryUser> {
                viewModel.login(it.phone, it.password)
            }
            onAddAccountClickListener = View.OnClickListener {
                accountNavigator.toAddNewAccount()
            }
            setDataSource(viewModel.historyUsers.toMutableList())
        }

        //recycler view
        with(vb.accountRvSwitchList) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = mSwitchAccountAdapter
        }
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            handleFlowData(viewModel.loginState) {
                onSuccess = {
                    showMessage(R.string.switch_success)
                    accountNavigator.exitAndToHomePage()
                }
            }
        }
    }

}