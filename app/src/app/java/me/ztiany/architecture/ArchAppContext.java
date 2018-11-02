package me.ztiany.architecture;


import com.app.base.AppContext;
import com.app.base.di.AppModule;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-02 16:32
 */
public class ArchAppContext extends AppContext {

    @Override
    protected void injectAppContext() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .injectAppContext(this);
    }

}
