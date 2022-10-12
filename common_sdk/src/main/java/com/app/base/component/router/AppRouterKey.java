package com.app.base.component.router;

import com.android.common.api.router.AppNavigator;

import dagger.MapKey;

@MapKey
public @interface AppRouterKey {

    Class<? extends AppNavigator> value();

}