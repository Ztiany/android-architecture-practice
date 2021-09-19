package com.app.base.widget.address;

import com.android.base.utils.common.Checker;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
class AddressItem implements IName {

    private AddressToken name;
    private List<AddressItem> children;

    public AddressToken getAddressToken() {
        return name;
    }

    public List<IName> getChildren() {
        if (Checker.isEmpty(children)) {
            return null;
        }
        return new ArrayList<>(children);
    }

}