package com.app.base.component.appservice;

import com.app.common.api.appservice.AppService;

import dagger.MapKey;

@MapKey
public @interface AppServiceKey {

    Class<? extends AppService> value();

}