package com.app.base.widget.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.base.adapter.recycler.RecyclerAdapter;
import com.android.base.adapter.recycler.ViewHolder;
import com.app.base.R;

class AddressAdapter extends RecyclerAdapter<IName, ViewHolder> {

    private View.OnClickListener mItemClickListener;

    void setItemClickListener(View.OnClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    AddressAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_address_item_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView nameView = viewHolder.helper().getView(R.id.dialog_address_tv_province);
        IName item = getItem(position);
        if (item != null) {
            IName.AddressToken addressToken = item.getAddressToken();
            nameView.setText(addressToken.getName());
            viewHolder.itemView.setTag(getItem(position));
            viewHolder.itemView.setOnClickListener(mItemClickListener);
        }
    }

}