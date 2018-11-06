package com.android.sdk.social.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwchina.sdk.social.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享 UI 默认实现。
 */
public class ShareFragmentDialog extends AppCompatDialogFragment {

    protected Share mShare;
    private View mLayoutView;
    protected FrameLayout mTopExtensionFl;

    private static final String SHARE_CONTENT_KEY = "share_content";
    private static final String SHARE_CONTENT_PLATFORM = "share_content_platform";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_ShareDialog);
        if (getArguments() != null) {
            mShare = getArguments().getParcelable(SHARE_CONTENT_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView == null) {
            mLayoutView = inflater.inflate(R.layout.social_dialog_share, container, false);
            mTopExtensionFl = mLayoutView.findViewById(R.id.dialog_share_fl_top_extension);
            setupViews();
        }
        return mLayoutView;
    }

    static ShareFragmentDialog newInstance(Share shareContent, List<SharePlatform> sharePlatformList) {
        ShareFragmentDialog shareFragmentDialog = new ShareFragmentDialog();
        Bundle args = new Bundle();
        args.putParcelable(ShareFragmentDialog.SHARE_CONTENT_KEY, shareContent);
        ArrayList<SharePlatform> list = (sharePlatformList instanceof ArrayList) ? (ArrayList<SharePlatform>) sharePlatformList : new ArrayList<>(sharePlatformList);
        args.putParcelableArrayList(ShareFragmentDialog.SHARE_CONTENT_PLATFORM, list);
        shareFragmentDialog.setArguments(args);
        return shareFragmentDialog;
    }

    private void setupViews() {
        RecyclerView recyclerView = mLayoutView.findViewById(R.id.dialog_share_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        PlatformAdapter adapter = new PlatformAdapter(getPlatformList());
        recyclerView.setAdapter(adapter);
    }

    private List<SharePlatform> getPlatformList() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelableArrayList(SHARE_CONTENT_PLATFORM);
        }
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        AppCompatDialog dialog = new AppCompatDialog(getActivity(), getTheme());
        if (context == null) {
            return dialog;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return dialog;
        }
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            lp.width = outMetrics.widthPixels;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);
        }
        return dialog;
    }

    private View.OnClickListener mSharePlatformOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharePlatform sharePlatform = (SharePlatform) view.getTag();
            SocialShareHelper.handleShare(getActivity(), sharePlatform.mPlatformId, mShare);
            dismiss();
        }
    };

    ///////////////////////////////////////////////////////////////////////////
    // Adapter
    ///////////////////////////////////////////////////////////////////////////
    private class PlatformAdapter extends RecyclerView.Adapter<ShareViewHolder> {

        private final List<SharePlatform> mData;

        PlatformAdapter(List<SharePlatform> data) {
            mData = data;
        }

        @NonNull
        @Override
        public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.social_item_share, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
            SharePlatform item = mData.get(position);
            holder.icon.setImageResource(item.getDrawableId());
            holder.name.setText(item.getPlatformName());
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(mSharePlatformOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

    }

    private class ShareViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;

        ShareViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.dialog_share_iv_icon);
            name = itemView.findViewById(R.id.dialog_share_tv_name);
        }

    }

}
