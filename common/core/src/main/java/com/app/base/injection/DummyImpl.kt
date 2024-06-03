package com.app.base.injection

import com.app.common.api.router.AppNavigator
import com.app.common.api.appservice.AppService

interface DummyAppService : AppService
class DummyAppServiceImpl : DummyAppService

interface DummyNavigator : AppNavigator
class DummyNavigatorImpl : DummyNavigator