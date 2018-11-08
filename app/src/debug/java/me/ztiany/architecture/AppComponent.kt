package me.ztiany.architecture

import com.app.base.di.AppModule
import com.app.base.di.DataModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import me.ztiany.arch.home.main.HomeModule
import javax.inject.Singleton


/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-10-12 21:53
 */
@Component(modules = [AppModule::class, DataModule::class, AndroidInjectionModule::class, AndroidSupportInjectionModule::class, HomeModule::class])
@Singleton
interface AppComponent {

    fun injectAppContext(archAppContext: ArchAppContext)

}