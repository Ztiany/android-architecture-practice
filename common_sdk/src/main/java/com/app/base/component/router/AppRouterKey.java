package com.app.base.component.router;

import com.app.common.api.router.AppNavigator;

import dagger.MapKey;

@MapKey
public @interface AppRouterKey {

    Class<? extends AppNavigator> value();

}