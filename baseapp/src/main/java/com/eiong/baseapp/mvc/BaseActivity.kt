package com.eiong.baseapp.mvc

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.billy.android.loading.Gloading
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ReflectUtils
import com.eiong.baseapp.manager.BaseManager
import java.lang.reflect.ParameterizedType

/**
 * MVC
 *
 * @author EIong
 *
 * @param VB 绑定视图
 */
open class BaseActivity<VB : ViewBinding> : BaseView, AppCompatActivity() {
    /** 请求权限提示 */
    private var permissionsTip = ""

    /** 取消按钮文本 */
    private var cancel = ""

    /** 确定按钮文本 */
    private var confirm = ""

    /** 点击取消回调 */
    private var onCancel = {}

    /** 获取所有权限回调 */
    private var onGrantedAll = {}

    /** 请求的权限 */
    private var permissions = emptyArray<String>()

    /** 是否从应用设置回来 */
    private var isFromAppDetailsSettings = false

    /** 根视图加载状态 */
    private lateinit var loadingHolder: Gloading.Holder

    /** 多视图加载状态 */
    private val loadingHoldersOverView = hashMapOf<View, Gloading.Holder>()

    /** 绑定视图对象 */
    protected val vb: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val vbClass = type.actualTypeArguments[0] as Class<*>
        ReflectUtils.reflect(vbClass).method("inflate", layoutInflater).get<VB>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        Gloading.initDefault(getLoadingAdapter())
        loadingHolder = Gloading.getDefault().wrap(this)
        initialize(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (isFromAppDetailsSettings) {
            isFromAppDetailsSettings = false
            requestPermissions(
                permissionsTip,
                cancel,
                confirm,
                onCancel,
                onGrantedAll,
                *permissions
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingHoldersOverView.clear()
    }

    override fun getResources(): Resources {
        BaseManager.apply {
            return if (isOpenAdaptScreen) {
                when (adaptScreenDirection) {
                    BaseManager.Direction.HEIGHT -> {
                        AdaptScreenUtils.adaptHeight(super.getResources(), adaptScreenValue)
                    }
                    BaseManager.Direction.WIDTH -> {
                        AdaptScreenUtils.adaptWidth(super.getResources(), adaptScreenValue)
                    }
                }
            } else {
                AdaptScreenUtils.closeAdapt(super.getResources())
            }
        }
    }

    override fun requestPermissions(
        permissionsTip: String,
        cancel: String,
        confirm: String,
        onCancel: () -> Unit,
        onGrantedAll: () -> Unit,
        vararg permissions: String
    ) {
        this.permissionsTip = permissionsTip
        this.cancel = cancel
        this.confirm = confirm
        this.onCancel = onCancel
        this.onGrantedAll = onGrantedAll
        @Suppress("UNCHECKED_CAST")
        this.permissions = permissions as Array<String>

        PermissionUtils
            .permission(*permissions)
            .explain { activity, denied, shouldRequest ->
                showBaseDialog(
                    activity,
                    permissionsTip,
                    cancel,
                    confirm,
                    {
                        shouldRequest.start(false)
                        onCancel()
                    },
                    {
                        shouldRequest.start(true)
                    }
                )
            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (deniedForever.isNotEmpty()) {
                    showBaseDialog(
                        this,
                        "部分权限被永久拒绝，请去设置手动打开",
                        "取消",
                        "确定",
                        {
                            onCancel()
                        },
                        {
                            isFromAppDetailsSettings = true
                            PermissionUtils.launchAppDetailsSettings()
                        }
                    )
                } else if (!isAllGranted) {
                    requestPermissions(
                        permissionsTip,
                        cancel,
                        confirm,
                        onCancel,
                        onGrantedAll,
                        *permissions
                    )
                } else {
                    onGrantedAll()
                }
            }
            .request()
    }

    override fun showLoading() {
        loadingHolder.showLoading()
    }

    override fun showSuccess() {
        loadingHolder.showLoadSuccess()
    }

    override fun showFailed(retry: Runnable?) {
        loadingHolder.withRetry(retry).showLoadFailed()
    }

    override fun showEmpty(retry: Runnable?) {
        loadingHolder.withRetry(retry).showEmpty()
    }

    override fun showLoadingOverView(view: View) {
        if (!loadingHoldersOverView.containsKey(view)) {
            loadingHoldersOverView[view] = Gloading.getDefault().wrap(view)
        }
        loadingHoldersOverView[view]?.showLoading()
    }

    override fun showSuccessOverView(view: View) {
        if (!loadingHoldersOverView.containsKey(view)) {
            loadingHoldersOverView[view] = Gloading.getDefault().wrap(view)
        }
        loadingHoldersOverView[view]?.showLoadSuccess()
    }

    override fun showFailedOverView(view: View, retry: Runnable?) {
        if (!loadingHoldersOverView.containsKey(view)) {
            loadingHoldersOverView[view] = Gloading.getDefault().wrap(view)
        }
        loadingHoldersOverView[view]?.withRetry(retry)?.showLoadFailed()
    }

    override fun showEmptyOverView(view: View, retry: Runnable?) {
        if (!loadingHoldersOverView.containsKey(view)) {
            loadingHoldersOverView[view] = Gloading.getDefault().wrap(view)
        }
        loadingHoldersOverView[view]?.withRetry(retry)?.showEmpty()
    }
}