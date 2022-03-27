package com.eiong.baseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isGone
import com.billy.android.loading.Gloading
import com.eiong.baseapp.databinding.LayoutBaseLoadingBinding

/**
 * 状态加载
 *
 * @author EIong
 */
class BaseLoadingAdapter : Gloading.Adapter {
    override fun getView(holder: Gloading.Holder?, convertView: View?, status: Int): View {
        var baseLoadingView: BaseLoadingView? = null
        if (convertView != null && convertView is BaseLoadingView) {
            baseLoadingView = convertView
        }
        if (baseLoadingView == null) {
            baseLoadingView = BaseLoadingView(holder?.context, holder?.retryTask)
        }
        baseLoadingView.setStatus(status, holder?.retryTask)
        return baseLoadingView
    }

    /**
     * 加载View
     *
     * @param retry 重试操作
     */
    @SuppressLint("ViewConstructor")
    class BaseLoadingView(context: Context?, private var retry: Runnable?) :
        LinearLayout(context), View.OnClickListener {
        private val vb = LayoutBaseLoadingBinding.inflate(LayoutInflater.from(context))

        init {
            vb.apply {
                addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                root.setOnClickListener(this@BaseLoadingView)
            }
        }

        /**
         * 设置加载状态：加载中、加载成功、加载失败、无数据，还可自定义其他加载状态
         *
         * @param status 加载状态
         * @param retry  重试操作
         */
        fun setStatus(status: Int, retry: Runnable?) {
            this.retry = retry
            var show = true
            vb.apply {
                when (status) {
                    Gloading.STATUS_LOADING -> {
                        tvTip.text = "加载中..."
                    }
                    Gloading.STATUS_LOAD_SUCCESS -> {
                        show = false
                    }
                    Gloading.STATUS_LOAD_FAILED -> {
                        tvTip.text = "加载失败，点击重试"
                    }
                    Gloading.STATUS_EMPTY_DATA -> {
                        tvTip.text = "这里空空的，点击重试"
                    }
                    else -> {
                        show = false
                    }
                }
                visibility = if (show) VISIBLE else GONE
                pgbLoading.visibility = if (status == Gloading.STATUS_LOADING) VISIBLE else GONE
            }
        }

        override fun onClick(v: View) {
            vb.apply {
                when (v) {
                    root -> {
                        if (pgbLoading.isGone) {
                            retry?.run()
                        }
                    }
                }
            }
        }
    }
}