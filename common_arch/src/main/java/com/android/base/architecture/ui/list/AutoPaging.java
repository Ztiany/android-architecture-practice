package com.android.base.architecture.ui.list;

import com.android.base.adapter.DataManager;

/**
 * @author Ztiany
 */
@SuppressWarnings("rawtypes")
public class AutoPaging extends Paging {

    private final DataManager mDataManager;
    private final ListLayoutHost mRefreshListLayoutHost;

    public AutoPaging(ListLayoutHost refreshListLayoutHost, DataManager dataManager) {
        mRefreshListLayoutHost = refreshListLayoutHost;
        mDataManager = dataManager;
    }

    @Override
    public int getCurrentPage() {
        return calcPageNumber(mDataManager.getDataSize());
    }

    @Override
    public int getLoadingPage() {
        if (mRefreshListLayoutHost.isRefreshing()) {
            return getPageStart();
        } else {
            return getCurrentPage() + 1;
        }
    }

    @Override
    public int getItemCount() {
        return mDataManager.getDataSize();
    }

}
