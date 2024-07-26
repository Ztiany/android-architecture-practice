package com.app.base.component.router;

import com.app.common.api.router.Navigator;

import dagger.MapKey;

@MapKey
public @interface NavigatorKey {

    Class<? extends Navigator> value();

}