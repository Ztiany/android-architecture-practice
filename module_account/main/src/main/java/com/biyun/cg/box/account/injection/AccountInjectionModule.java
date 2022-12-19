package com.biyun.cg.box.account.injection;

import com.app.common.api.network.ApiServiceFactoryProvider;
import com.biyun.cg.box.account.data.AccountApi;
import com.biyun.cg.box.account.data.AccountDataSource;
import com.biyun.cg.box.account.data.AccountRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class AccountInjectionModule {

    @Provides
    static AccountApi provideAccountApi(ApiServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.getDefault().create(AccountApi.class);
    }

    @Provides
    @ActivityRetainedScoped
    static AccountDataSource provideAccountDataSource(AccountRepository accountRepository) {
        return accountRepository;
    }

}