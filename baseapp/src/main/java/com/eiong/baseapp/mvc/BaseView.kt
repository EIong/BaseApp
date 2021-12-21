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
     * 初始化
     */
    fun initialize(savedInstanceState: Bundle?) {}

    /**
     * 获取加载状态适配器
     *
     * @return 加载状态适配器
     */
    fun getLoadingAdapter(): Gloading.Adapter {
        return BaseManager.loadingAdapter
    }

    /**
     * 弹出消息对话框
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
     * 加载中
     */
    fun showLoading()

    /**
     * 加载成功
     */
    fun showSuccess()

    /**
     * 加载失败
     *
     * @param retry 重试任务
     */
    fun showFailed(retry: Runnable?)

    /**
     * 无数据
     *
     * @param retry 重试任务
     */
    fun showEmpty(retry: Runnable?)

    /**
     * 加载中（指定视图）
     *
     * @param view 指定的视图
     */
    fun showLoadingOverView(view: View)

    /**
     * 加载成功（指定视图）
     *
     * @param view 指定的视图
     */
    fun showSuccessOverView(view: View)

    /**
     * 加载失败（指定视图）
     *
     * @param view  指定的视图
     * @param retry 重试任务
     */
    fun showFailedOverView(view: View, retry: Runnable?)

    /**
     * 无数据（指定视图）
     *
     * @param view  指定的视图
     * @param retry 重试任务
     */
    fun showEmptyOverView(view: View, retry: Runnable?)

    /**
     * 添加点击事件
     *
     * @param views 添加点击事件的视图
     */
    fun addClick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {}

    /**
     * 显示视图
     *
     * @param views 显示的视图
     */
    fun showViews(vararg views: View) {
        views.forEach {
            it.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏视图（INVISIBLE）
     *
     * @param views 隐藏的视图
     */
    fun hideViews(vararg views: View) {
        views.forEach {
            it.visibility = View.INVISIBLE
        }
    }

    /**
     * 隐藏视图（GONE）
     *
     * @param views 隐藏的视图
     */
    fun goneViews(vararg views: View) {
        views.forEach {
            it.visibility = View.GONE
        }
    }
}