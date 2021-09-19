package com.app.base.widget.address

import androidx.fragment.app.FragmentManager

fun showAddressPicker(fragmentManager: FragmentManager, selectAddressListener: AddressListener) {
    AddressPicker()
            .setAddressListener(selectAddressListener)
            .show(fragmentManager, AddressPicker::class.java.name)
}

