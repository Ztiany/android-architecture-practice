package me.ztiany.arch.home.main.injection

import androidx.lifecycle.ViewModel
import com.android.base.app.dagger.ActivityScope
import com.android.base.app.dagger.FragmentScope
import com.android.base.app.dagger.ViewModelKey
import com.android.base.app.dagger.ViewModelModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.ztiany.arch.home.main.MainActivity
import me.ztiany.arch.home.main.MainFragment
import me.ztiany.arch.home.main.data.MainDataSource
import me.ztiany.arch.home.main.data.MainRepository
import me.ztiany.arch.home.main.presentation.index.IndexFragment
import me.ztiany.arch.home.main.presentation.index.IndexViewModule
import me.ztiany.arch.home.main.presentation.middle.MiddleFragment
import me.ztiany.arch.home.main.presentation.middle.MiddleViewModel
import me.ztiany.arch.home.main.presentation.mine.MineFragment
import me.ztiany.arch.home.main.presentation.mine.MineViewModel

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
abstract class HomeModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewModelModule::class, SubModule::class])
    internal abstract fun contributeMainActivityInjector(): MainActivity

}

@Module
abstract class SubModule {

    @Binds
    @IntoMap
    @ViewModelKey(IndexViewModule::class)
    abstract fun provideIndexViewModule(indexViewModule: IndexViewModule): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MineViewModel::class)
    abstract fun provideMineViewModule(mineViewModel: MineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MiddleViewModel::class)
    abstract fun provideAccompanyViewModule(middleViewModule: MiddleViewModel): ViewModel

    @Binds
    @ActivityScope
    abstract fun provideMainDataSource(indexRepository: MainRepository): MainDataSource

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMainFragmentInjector(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeIndexFragmentInjector(): IndexFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMineFragmentInjector(): MineFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeAccompanyFragmentInjector(): MiddleFragment

}