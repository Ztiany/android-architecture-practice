package me.ztiany.arch.home.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.base.app.fragment.tools.FragmentInfo;
import com.android.base.app.fragment.tools.TabManager;

import me.ztiany.arch.home.main.index.presentation.IndexFragment;
import me.ztiany.arch.home.main.middle.MiddleFragment;
import me.ztiany.arch.home.main.mine.presentation.MineFragment;
import me.ztiany.architecture.home.R;
import timber.log.Timber;

final class MainTabManager extends TabManager {

    @Override
    protected void onFragmentCreated(int id, Fragment newFragment) {
        super.onFragmentCreated(id, newFragment);
        Timber.d("onFragmentCreated() called with: id = [" + id + "], newFragment = [" + newFragment + "]");
    }

    MainTabManager(Context context, FragmentManager fragmentManager, int containerId) {
        super(context, fragmentManager, new MainTabs(), containerId);
    }

    private static final class MainTabs extends Tabs {

        MainTabs() {

            int[] TAB_ID = {
                    R.id.main_index,
                    R.id.main_middle,
                    R.id.main_mine
            };

            add(new FragmentInfo.PageBuilder()
                    .clazz(IndexFragment.class)
                    .tag(IndexFragment.class.getName())
                    .toStack(false)
                    .pagerId(TAB_ID[0])
                    .build());

            add(new FragmentInfo.PageBuilder()
                    .clazz(MiddleFragment.class)
                    .tag(MiddleFragment.class.getName())
                    .toStack(false)
                    .pagerId(TAB_ID[1])
                    .build());


            add(new FragmentInfo.PageBuilder()
                    .clazz(MineFragment.class)
                    .tag(MineFragment.class.getName())
                    .toStack(false)
                    .pagerId(TAB_ID[2])
                    .build());

        }
    }


}
