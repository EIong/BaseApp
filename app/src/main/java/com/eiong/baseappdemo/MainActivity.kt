package com.eiong.baseappdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.manager.BaseManager
import com.eiong.baseapp.mvc.BaseActivity
import com.eiong.baseappdemo.databinding.ActivityMainBinding
import com.eiong.baseappdemo.mvc.MvcActivity
import com.eiong.baseappdemo.mvp.MvpActivity

/**
 * 首页
 *
 * @author EIong
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        vb.apply {
            addClick(cdvMvc, cdvMvp, cdvMakeLog, cdvViewLog)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        vb.apply {
            when (v) {
                cdvMvc -> {
                    startActivity(Intent(this@MainActivity, MvcActivity::class.java))
                }
                cdvMvp -> {
                    startActivity(Intent(this@MainActivity, MvpActivity::class.java))
                }
                cdvMakeLog -> {
                    LogUtils.d("这是 ${TimeUtils.getNowString()} 生成的日志")
                    ToastUtils.showShort("操作成功")
                }
                cdvViewLog -> {
                    BaseManager.goToLog(this@MainActivity, "1111111111111111")
                }
            }
        }
    }
}