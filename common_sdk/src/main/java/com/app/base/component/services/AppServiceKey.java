package com.app.base.component.services;

import com.android.common.api.services.AppService;

import dagger.MapKey;

@MapKey
public @interface AppServiceKey {

    Class<? extends AppService> value();

}