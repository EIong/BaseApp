package com.eiong.baseappdemo.mvc

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.mvc.BaseActivity
import com.eiong.baseappdemo.R
import com.eiong.baseappdemo.databinding.ActivityMvcBinding

/**
 * MVC
 *
 * @author EIong
 */
class MvcActivity : BaseActivity<ActivityMvcBinding>() {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        supportActionBar?.apply {
            title = "MVC"
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
        vb.apply {
            when (v) {
                cdvLoadFragmentStatic -> {
                    startActivity(Intent(this@MvcActivity, MvcFraActivity::class.java))
                }
                cdvLoadFragmentDynamic -> {
                    FragmentUtils.replace(supportFragmentManager, MvcFragment(), R.id.flContent)
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
        Handler(mainLooper).postDelayed(
            {
                when ((Math.random() * 3).toInt()) {
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
                        showSuccess()
                    }
                }
            }, 2000
        )
    }
}