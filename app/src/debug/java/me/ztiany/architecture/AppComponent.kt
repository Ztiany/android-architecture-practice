package me.ztiany.architecture

import com.app.base.data.DataModule
import com.app.base.di.AppModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-10-12 21:53
 */
@Component(modules = [AppModule::class, DataModule::class, AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
@Singleton
interface AppComponent {

    fun injectAppContext(archAppContext: ArchAppContext)

}