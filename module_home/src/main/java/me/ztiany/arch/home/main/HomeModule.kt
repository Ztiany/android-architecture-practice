package me.ztiany.arch.home.main

import android.arch.lifecycle.ViewModel
import com.android.base.app.dagger.ActivityScope
import com.android.base.app.dagger.FragmentScope
import com.android.base.app.dagger.ViewModelKey
import com.android.base.app.dagger.ViewModelModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.ztiany.arch.home.main.index.data.IndexDataSource
import me.ztiany.arch.home.main.index.data.IndexRepository
import me.ztiany.arch.home.main.index.presentation.IndexFragment
import me.ztiany.arch.home.main.index.presentation.IndexViewModule
import me.ztiany.arch.home.main.middle.MiddleFragment
import me.ztiany.arch.home.main.middle.MiddleViewModule
import me.ztiany.arch.home.main.mine.data.MineDataSource
import me.ztiany.arch.home.main.mine.data.MineRepository
import me.ztiany.arch.home.main.mine.presentation.MineFragment
import me.ztiany.arch.home.main.mine.presentation.MineViewModel

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
    @ActivityScope
    abstract fun provideIndexDataSource(indexRepository: IndexRepository): IndexDataSource

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun contributeIndexFragmentInjector(): IndexFragment

    @Binds
    @ActivityScope
    abstract fun provideMineDataSource(mineRepository: MineRepository): MineDataSource

    @Binds
    @IntoMap
    @ViewModelKey(MineViewModel::class)
    abstract fun provideMineViewModule(mineViewModel: MineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MiddleViewModule::class)
    abstract fun provideAccompanyViewModule(middleViewModule: MiddleViewModule): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMineFragmentInjector(): MineFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeAccompanyFragmentInjector(): MiddleFragment

}