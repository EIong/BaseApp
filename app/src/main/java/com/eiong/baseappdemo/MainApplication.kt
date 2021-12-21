package com.eiong.baseappdemo

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.eiong.baseapp.manager.BaseManager

/**
 * @author EIong
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.getConfig()
            .setSaveDays(3)
            .isLog2FileSwitch = true
        BaseManager.setOpenAdaptScreen(true)
            .setAdaptScreenDirection(BaseManager.Direction.WIDTH)
            .setAdaptScreenValue(1080)
            .enableLogEncryption("1111111111111111")
    }
}