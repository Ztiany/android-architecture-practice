package com.app.base.widget.address;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.base.utils.android.adaption.OnItemClickListener;
import com.android.base.utils.android.adaption.OnTabSelectedListenerAdapter;
import com.android.base.utils.android.WindowUtils;
import com.android.base.utils.common.Checker;
import com.app.base.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * 选择地区
 *
 * @author Ztiany
 * @version 1.0
 * Email: 1169654504@qq.com
 */
public class AddressPicker extends DialogFragment implements AddressInquirers.AddressQueryCallback {

    private TabLayout mTabLayout;

    private AddressListener mAddressListener;

    /**
     * 国家->省->市->区
     */
    private static final int FIX_ADDRESS_ITEM_COUNT = 4;

    private AddressInquirers mAddressInquirers;
    private AddressAdapter mAddressAdapter;
    private List<IName> mSource;
    private List<IName> mCurrentSource;

    private IName.AddressToken[] mAddressItems = new IName.AddressToken[FIX_ADDRESS_ITEM_COUNT];
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ThemeDialogCommon_Floating);
        mAddressInquirers = new AddressInquirers();
        mAddressInquirers.setAddressQueryCallback(this);
        mAddressAdapter = new AddressAdapter(getContext());
        mAddressAdapter.setItemClickListener(mItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = View.inflate(requireContext(), R.layout.dialog_address_picker, null);
        mRecyclerView = layoutView.findViewById(R.id.dialog_address_rv_content);
        mTabLayout = layoutView.findViewById(R.id.dialog_address_tbl_tab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAddressAdapter);
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListenerAdapter);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window dialogWindow = requireDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = (int) (WindowUtils.getScreenHeight() * 0.6F);
            dialogWindow.setAttributes(lp);
        }
        if (Checker.isEmpty(mSource)) {
            mAddressInquirers.start();
        }
    }

    AddressPicker setAddressListener(AddressListener addressListener) {
        mAddressListener = addressListener;
        return this;
    }

    @Override
    public void onGetAddress(List<IName> names) {
        mSource = names;
        startShowAddress();
    }

    private void startShowAddress() {
        mCurrentSource = mSource;
        showNext(true);
    }

    private void addTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText(R.string.please_select);
        mTabLayout.addTab(tab);
        tab.setTag(mCurrentSource);
        tab.select();
    }

    private void showNext(boolean addTab) {
        if (addTab) {
            addTab();
        }
        mAddressAdapter.replaceAll(mCurrentSource);
        mRecyclerView.scrollToPosition(0);
    }

    private OnTabSelectedListenerAdapter mOnTabSelectedListenerAdapter = new OnTabSelectedListenerAdapter() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (mTabLayout.getTabAt(mTabLayout.getTabCount() - 1) == tab) {//最后一个
                return;
            }

            @SuppressWarnings("unchecked")
            List<IName> iNames = (List<IName>) tab.getTag();
            //找到tab位置
            int index = 0;
            int tabCount = mTabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                if (tab == mTabLayout.getTabAt(i)) {
                    index = i;
                    break;
                }
            }
            //去掉被点击tab后面的tab
            for (int i = tabCount - 1; i > index; i--) {
                mTabLayout.removeTabAt(i);
            }

            //重置
            mCurrentSource = iNames;
            for (int i = FIX_ADDRESS_ITEM_COUNT - 1; i >= index; i--) {
                mAddressItems[i] = null;
            }
            tab.setText(R.string.please_select);
            showNext(false);
        }
    };

    private void onSelectCompleted() {
        if (mAddressListener != null) {
            mAddressListener.onSelectedAddress(
                    getAddressNameByIndex(1),
                    getAddressNameByIndex(2),
                    getAddressNameByIndex(3)
            );
        }
        dismiss();
    }

    private OnItemClickListener<IName> mItemClickListener = new OnItemClickListener<IName>() {

        @Override
        public void onClick(@NotNull View view, IName iName) {

            List<IName> children = iName.getChildren();
            IName.AddressToken name = iName.getAddressToken();
            TabLayout.Tab tabAt = mTabLayout.getTabAt(mTabLayout.getTabCount() - 1);
            setSelectedItem(name);
            assert tabAt != null;
            tabAt.setText(name.getName());

            if (!Checker.isEmpty(children)) {
                mCurrentSource = children;
                showNext(true);
            } else {
                onSelectCompleted();
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        Window dialogWindow = dialog.getWindow();

        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setWindowAnimations(R.style.StyleAnimBottomIn);
            dialogWindow.setAttributes(lp);
        }

        return dialog;
    }

    @Override
    public void onDismiss(@NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mAddressInquirers != null) {
            mAddressInquirers.destroy();
        }
    }

    private void setSelectedItem(IName.AddressToken token) {
        int index = -1;
        if (token.getIdentifying() == IName.AddressToken.COUNTRY) {
            index = 0;
        } else if (token.getIdentifying() == IName.AddressToken.PROVINCE) {
            index = 1;
        } else if (token.getIdentifying() == IName.AddressToken.CITY) {
            index = 2;
        } else if (token.getIdentifying() == IName.AddressToken.AREA) {
            index = 3;
        }
        if (index != -1) {
            mAddressItems[index] = token;
        }
    }

    private String getAddressNameByIndex(int index) {
        IName.AddressToken token = mAddressItems[index];
        return token == null ? "" : token.getName();
    }

}