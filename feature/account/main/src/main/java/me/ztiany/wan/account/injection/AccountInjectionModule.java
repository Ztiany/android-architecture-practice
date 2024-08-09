package me.ztiany.wan.account.injection;

import com.app.common.api.network.ServiceFactoryProvider;
import me.ztiany.wan.account.data.AccountApi;
import me.ztiany.wan.account.data.AccountDataSource;
import me.ztiany.wan.account.data.AccountRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class AccountInjectionModule {

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