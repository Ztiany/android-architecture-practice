package me.ztiany.wan.account.injection;

import com.app.common.api.network.ServiceFactoryProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import me.ztiany.wan.account.data.AccountApi;
import me.ztiany.wan.account.data.AccountDataSource;
import me.ztiany.wan.account.data.AccountRepository;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class AccountActivityModule {

    @Provides
    static AccountApi provideAccountApi(ServiceFactoryProvider serviceFactoryProvider) {
        return serviceFactoryProvider.getDefault().createDefault(AccountApi.class);
    }

    @Provides
    @ActivityRetainedScoped
    static AccountDataSource provideAccountDataSource(AccountRepository accountRepository) {
        return accountRepository;
    }

}