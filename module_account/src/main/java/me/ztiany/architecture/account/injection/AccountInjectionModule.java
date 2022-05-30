package me.ztiany.architecture.account.injection;

import com.app.base.app.ServiceProvider;
import me.ztiany.architecture.account.data.AccountApi;
import me.ztiany.architecture.account.data.AccountDataSource;
import me.ztiany.architecture.account.data.AccountRepository;

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