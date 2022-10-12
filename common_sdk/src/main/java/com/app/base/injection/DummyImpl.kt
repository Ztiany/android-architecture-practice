package com.app.base.injection

import com.android.common.api.router.AppNavigator
import com.android.common.api.services.AppService

interface DummyAppService : AppService
class DummyAppServiceImpl : DummyAppService

interface DummyNavigator : AppNavigator
class DummyNavigatorImpl : DummyNavigator