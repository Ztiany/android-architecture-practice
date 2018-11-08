package me.ztiany.architecture.home;


import com.app.base.AppContext;
import com.app.base.di.AppModule;

public class HomeAppContext extends AppContext {

    @Override
    protected void injectAppContext() {
        DaggerHomeAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

}