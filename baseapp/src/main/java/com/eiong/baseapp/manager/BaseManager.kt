package com.eiong.baseapp.manager

import android.app.Activity
import android.content.Intent
import com.billy.android.loading.Gloading
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.LogUtils
import com.eiong.baseapp.adapter.BaseLoadingAdapter
import com.eiong.baseapp.ui.ViewLogActivity

/**
 * 管理
 *
 * @author EIong
 */
object BaseManager {
    /** 屏幕适配方向（高度或宽度） */
    enum class Direction {
        HEIGHT, WIDTH
    }

    /** 是否开启屏幕适配（默认关闭） */
    var isOpenAdaptScreen = false

    /** 屏幕适配方向（默认宽度） */
    var adaptScreenDirection = Direction.WIDTH

    /** 屏幕适配参数（默认1080） */
    var adaptScreenValue = 1080

    /** 加载状态适配器（默认[BaseLoadingAdapter]） */
    var loadingAdapter: Gloading.Adapter = BaseLoadingAdapter()

    /**
     * 设置是否开启屏幕适配（默认关闭）
     *
     * @param open 是否开启屏幕适配
     *
     * @return [BaseManager]
     */
    fun setOpenAdaptScreen(open: Boolean): BaseManager {
        isOpenAdaptScreen = open
        return this
    }

    /**
     * 设置屏幕适配方向（默认宽度）
     *
     * @param direction 屏幕适配方向
     *
     * @return [BaseManager]
     */
    fun setAdaptScreenDirection(direction: Direction): BaseManager {
        adaptScreenDirection = direction
        return this
    }

    /**
     * 设置屏幕适配参数（默认1080）
     *
     * @param value 屏幕适配参数
     *
     * @return [BaseManager]
     */
    fun setAdaptScreenValue(value: Int): BaseManager {
        if (value <= 0) {
            throw Exception("参数应大于0")
        }
        adaptScreenValue = value
        return this
    }

    /**
     * 设置加载状态适配器（默认[BaseLoadingAdapter]）
     *
     * @param adapter 加载状态适配器
     *
     * @return [BaseManager]
     */
    fun setLoadingAdapter(adapter: Gloading.Adapter): BaseManager {
        loadingAdapter = adapter
        return this
    }

    /**
     * 开启日志加密
     *
     * @param password 密码
     *
     * @return [BaseManager]
     */
    fun enableLogEncryption(password: String): BaseManager {
        LogUtils.getConfig().setFileWriter { file, content ->
            FileIOUtils.writeFileFromString(
                file,
                EncryptUtils.encryptAES2Base64(
                    content.toByteArray(),
                    password.toByteArray(),
                    "AES/ECB/PKCS5Padding",
                    null
                ).decodeToString().plus("|"),
                true
            )
        }
        return this
    }

    /**
     * 跳转查看日志
     *
     * @param password 密码
     */
    fun goToLog(activity: Activity, password: String) {
        activity.startActivity(Intent(activity, ViewLogActivity::class.java).apply {
            putExtra("password", password)
        })
    }
}