package me.ztiany.architecture.home;


import com.app.base.di.AppModule;
import com.app.base.di.DataModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import me.ztiany.arch.home.main.injection.HomeModule;

@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                AndroidInjectionModule.class,
                AndroidSupportInjectionModule.class,
                //HomeBuilderModule
                HomeModule.class,
                TestBuilderModule.class
        }
)
@Singleton
interface HomeAppComponent {

    void inject(HomeAppContext homeAppContext);

}