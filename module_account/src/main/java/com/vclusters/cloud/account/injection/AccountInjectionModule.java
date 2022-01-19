package com.vclusters.cloud.account.injection;

import com.app.base.app.ServiceProvider;
import com.vclusters.cloud.account.data.AccountApi;
import com.vclusters.cloud.account.data.AccountDataSource;
import com.vclusters.cloud.account.data.AccountRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class AccountInjectionModule {

    @Provides
    static AccountApi provideAccountApi(ServiceProvider serviceProvider) {
        return serviceProvider.getDefault().create(AccountApi.class);
    }

    @Provides
    @ActivityRetainedScoped
    static AccountDataSource provideAccountDataSource(AccountRepository accountRepository) {
        return accountRepository;
    }

}