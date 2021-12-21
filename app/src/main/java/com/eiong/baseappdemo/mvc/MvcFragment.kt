package com.eiong.baseappdemo.mvc

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.mvc.BaseFragment
import com.eiong.baseappdemo.databinding.FragmentMvcBinding

/**
 * MVC
 *
 * @author EIong
 */
class MvcFragment : BaseFragment<FragmentMvcBinding>() {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        vb.apply {
            addClick(cdvRequestPermission, cdvRequestData)
        }
        requestData()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        vb.apply {
            when (v) {
                cdvRequestPermission -> {
                    requestPermissions(
                        "应用需要以下权限以正常工作：\n" +
                                "- 读取和写入内部存储权限\n" +
                                "- 相机权限",
                        "取消",
                        "确定",
                        {},
                        {
                            ToastUtils.showShort("请求权限成功")
                        },
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
                cdvRequestData -> {
                    requestData()
                }
            }
        }
    }

    /**
     * 请求数据
     */
    private fun requestData() {
        showLoading()
        val color = (-(Math.random() * (16777216 - 1) + 1)).toInt()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                when (-color % 3) {
                    0 -> {
                        showFailed {
                            requestData()
                        }
                    }
                    1 -> {
                        showEmpty {
                            requestData()
                        }
                    }
                    2 -> {
                        vb.root.setBackgroundColor(color)
                        showSuccess()
                    }
                }
            }, 2000
        )
    }
}