package com.eiong.baseappdemo.mvp

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.mvp.BaseFragment
import com.eiong.baseappdemo.databinding.FragmentMvpBinding

/**
 * MVP
 *
 * @author EIong
 */
class MvpFragment : BaseFragment<FragmentMvpBinding, MvpPresenter>(), MvpContract.MvpView {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        vb.apply {
            addClick(cdvRequestPermission, cdvRequestData)
        }
        showLoading()
        p.requestData()
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
                    showLoading()
                    p.requestData()
                }
            }
        }
    }

    override fun handleData(data: Int) {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                when (-data % 3) {
                    0 -> {
                        showFailed {
                            showLoading()
                            p.requestData()
                        }
                    }
                    1 -> {
                        showEmpty {
                            showLoading()
                            p.requestData()
                        }
                    }
                    2 -> {
                        vb.root.setBackgroundColor(data)
                        showSuccess()
                    }
                }
            }, 2000
        )
    }
}