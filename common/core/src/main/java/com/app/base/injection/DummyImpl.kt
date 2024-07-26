package com.app.base.injection

import com.app.common.api.router.Navigator
import com.app.common.api.appservice.AppService

interface DummyAppService : AppService
class DummyAppServiceImpl : DummyAppService

interface DummyNavigator : Navigator
class DummyNavigatorImpl : DummyNavigator