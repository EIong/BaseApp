package com.eiong.baseappdemo.mvp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.mvp.BaseActivity
import com.eiong.baseappdemo.R
import com.eiong.baseappdemo.databinding.ActivityMvpBinding

/**
 * MVP
 *
 * @author EIong
 */
class MvpActivity : BaseActivity<ActivityMvpBinding, MvpPresenter>(), MvpContract.MvpView {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        supportActionBar?.apply {
            title = "MVP"
            setDisplayHomeAsUpEnabled(true)
        }
        vb.apply {
            addClick(
                cdvLoadFragmentStatic,
                cdvLoadFragmentDynamic,
                cdvRequestPermission,
                cdvRequestData
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        vb.apply {
            when (v) {
                cdvLoadFragmentStatic -> {
                    startActivity(Intent(this@MvpActivity, MvpFraActivity::class.java))
                }
                cdvLoadFragmentDynamic -> {
                    FragmentUtils.replace(supportFragmentManager, MvpFragment(), R.id.flContent)
                }
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
        Handler(mainLooper).postDelayed(
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
                        showSuccess()
                    }
                }
            }, 2000
        )
    }
}