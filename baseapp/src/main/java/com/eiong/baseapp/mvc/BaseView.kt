package com.eiong.baseapp.mvc

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.billy.android.loading.Gloading
import com.eiong.baseapp.databinding.LayoutBaseDialogBinding
import com.eiong.baseapp.manager.BaseManager

/**
 * MVC
 *
 * @author EIong
 */
interface BaseView : View.OnClickListener {
    /**
     * 初始化操作
     */
    fun initialize(savedInstanceState: Bundle?) {}

    /**
     * 获取状态加载适配器
     *
     * @return 状态加载适配器
     */
    fun getLoadingAdapter(): Gloading.Adapter {
        return BaseManager.loadingAdapter
    }

    /**
     * 消息弹窗
     *
     * @param message   消息内容
     * @param cancel    取消按钮文本
     * @param confirm   确定按钮文本
     * @param onCancel  点击取消回调
     * @param onConfirm 点击确定回调
     */
    fun showBaseDialog(
        activity: Activity,
        message: String,
        cancel: String,
        confirm: String,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    ) {
        var alertDialog: AlertDialog? = null
        LayoutBaseDialogBinding.inflate(activity.layoutInflater).apply {
            tvMessage.text = message
            tvCancel.apply {
                text = cancel
                setOnClickListener {
                    alertDialog?.dismiss()
                    onCancel()
                }
            }
            tvConfirm.apply {
                text = confirm
                setOnClickListener {
                    alertDialog?.dismiss()
                    onConfirm()
                }
            }
            alertDialog = AlertDialog.Builder(activity).create().apply {
                setCancelable(false)
                setView(root)
                show()
                window?.apply {
                    setDimAmount(0.5f)
                    setBackgroundDrawableResource(android.R.color.transparent)
                    attributes = attributes.apply {
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }
            }
        }
    }

    /**
     * 请求权限
     *
     * @param permissionsTip 请求权限提示
     * @param cancel         取消按钮文本
     * @param confirm        确定按钮文本
     * @param onCancel       点击取消回调
     * @param onGrantedAll   获取所有权限回调
     * @param permissions    请求的权限
     */
    fun requestPermissions(
        permissionsTip: String,
        cancel: String,
        confirm: String,
        onCancel: () -> Unit,
        onGrantedAll: () -> Unit,
        vararg permissions: String
    )

    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 显示加载成功
     */
    fun showSuccess()

    /**
     * 显示加载失败
     *
     * @param retry 重试操作
     */
    fun showFailed(retry: Runnable?)

    /**
     * 显示无数据
     *
     * @param retry 重试操作
     */
    fun showEmpty(retry: Runnable?)

    /**
     * 指定View上显示加载中
     *
     * @param view 指定View
     */
    fun showLoadingOverView(view: View)

    /**
     * 指定View上显示加载成功
     *
     * @param view 指定View
     */
    fun showSuccessOverView(view: View)

    /**
     * 指定View上显示加载失败
     *
     * @param view  指定View
     * @param retry 重试操作
     */
    fun showFailedOverView(view: View, retry: Runnable?)

    /**
     * 指定View上显示无数据
     *
     * @param view  指定View
     * @param retry 重试操作
     */
    fun showEmptyOverView(view: View, retry: Runnable?)

    /**
     * 添加点击事件
     *
     * @param views 添加点击事件的View
     */
    fun addClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {}

    /**
     * 显示View
     *
     * @param views 显示的View
     */
    fun showViews(vararg views: View) {
        views.forEach {
            it.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏View（INVISIBLE）
     *
     * @param views 隐藏的View
     */
    fun hideViews(vararg views: View) {
        views.forEach {
            it.visibility = View.INVISIBLE
        }
    }

    /**
     * 隐藏View（GONE）
     *
     * @param views 隐藏的View
     */
    fun goneViews(vararg views: View) {
        views.forEach {
            it.visibility = View.GONE
        }
    }
}