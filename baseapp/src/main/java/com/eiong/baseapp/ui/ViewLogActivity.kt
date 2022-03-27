package com.eiong.baseapp.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.eiong.baseapp.adapter.LogAdapter
import com.eiong.baseapp.databinding.ActivityViewLogBinding
import com.eiong.baseapp.databinding.ItemLogTabBinding
import com.eiong.baseapp.mvc.BaseActivity

/**
 * 查看日志
 *
 * @author EIong
 */
class ViewLogActivity : BaseActivity<ActivityViewLogBinding>() {
    private lateinit var password: ByteArray // 密码
    private val logTabs = arrayListOf<TextView>() // 标签
    private val logs = mutableListOf<MutableList<String>>() // 日志
    private var currentLogIndex = 0 // 当前日志索引
    private val rvLayoutManager = LinearLayoutManager(this)
    private lateinit var rvAdapter: LogAdapter

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        supportActionBar?.apply {
            title = "日志"
            setDisplayHomeAsUpEnabled(true)
        }
        intent.getStringExtra("password").let {
            it ?: run {
                ToastUtils.showShort("密码为空")
                finish()
                return
            }
            password = it.toString().toByteArray()
        }
        showLoading()
        Thread {
            LogUtils.getLogFiles().apply {
                for (i in 0 until size) {
                    logTabs.add(
                        ItemLogTabBinding.inflate(layoutInflater).root.apply {
                            text = get(i).name
                            tag = 0
                            setOnClickListener { view ->
                                rvAdapter.setSearchResult("", -1)
                                logTabs[currentLogIndex].tag =
                                    rvLayoutManager.findLastVisibleItemPosition()
                                currentLogIndex = logTabs.indexOf(view)
                                rvAdapter.setData(logs[currentLogIndex])
                                vb.rvLog.scrollToPosition(view.tag as Int)
                                vb.skbLog.progress =
                                    (rvLayoutManager.findLastVisibleItemPosition() + 1) * 100 / logs[currentLogIndex].size
                                view.setBackgroundColor(Color.WHITE)
                                logTabs.filter { it != view }.forEach { logTab ->
                                    logTab.setBackgroundColor(Color.TRANSPARENT)
                                }
                            }
                        }
                    )
                    logs.add(mutableListOf())
                    FileIOUtils.readFile2String(get(i)).split("|").forEach {
                        try {
                            logs[i].add(
                                EncryptUtils.decryptBase64AES(
                                    it.toByteArray(),
                                    password,
                                    "AES/ECB/PKCS5Padding",
                                    null
                                ).decodeToString()
                            )
                        } catch (exception: Exception) {
                        }
                    }
                    if (logs[i].isEmpty()) {
                        logs[i].add("这里空空的，什么都没有")
                    }
                }
            }
            if (logs.isEmpty()) {
                runOnUiThread {
                    showEmpty {
                        initialize(savedInstanceState)
                    }
                }
                return@Thread
            }
            runOnUiThread {
                initView()
            }
        }.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        vb.apply {
            logTabs.forEach {
                llLogTab.addView(it)
            }
            logTabs[0].setBackgroundColor(Color.WHITE)
            rvLog.apply {
                layoutManager = rvLayoutManager
                adapter = LogAdapter(this@ViewLogActivity, logs[0]).apply {
                    rvAdapter = this
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        skbLog.progress =
                            (rvLayoutManager.findLastVisibleItemPosition() + 1) * 100 / logs[currentLogIndex].size
                    }
                })
            }
            skbLog.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvProgress.text = "$progress%"
                    if (fromUser) {
                        rvLog.scrollToPosition(
                            if (progress == 100) {
                                logs[currentLogIndex].size - 1
                            } else {
                                logs[currentLogIndex].size * progress / 100
                            }
                        )
                        skbLog.progress =
                            (rvLayoutManager.findLastVisibleItemPosition() + 1) * 100 / logs[currentLogIndex].size
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            addClick(tvSearchUp, tvSearchDown)
        }
        showSuccess()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        vb.apply {
            when (v) {
                tvSearchUp -> {
                    val key = etSearch.text.toString()
                    if (key.isEmpty()) {
                        ToastUtils.showShort("请输入关键字")
                        return
                    }
                    val searchPosition =
                        if (rvAdapter.searchPosition < rvLayoutManager.findFirstVisibleItemPosition() ||
                            rvAdapter.searchPosition > rvLayoutManager.findLastVisibleItemPosition()
                        ) {
                            rvLayoutManager.findLastVisibleItemPosition()
                        } else {
                            rvAdapter.searchPosition - 1
                        }
                    showLoading()
                    Thread {
                        for (i in searchPosition downTo 0) {
                            logs[currentLogIndex][i].apply {
                                if (contains(key)) {
                                    runOnUiThread {
                                        rvLog.scrollToPosition(i)
                                        rvAdapter.setSearchResult(key, i)
                                        showSuccess()
                                    }
                                    return@Thread
                                }
                            }
                        }
                        ToastUtils.showShort("往上没有了")
                        runOnUiThread {
                            if (key != rvAdapter.searchKey) {
                                rvAdapter.setSearchResult("", -1)
                            }
                            showSuccess()
                        }
                    }.start()
                }
                tvSearchDown -> {
                    val key = etSearch.text.toString()
                    if (key.isEmpty()) {
                        ToastUtils.showShort("请输入关键字")
                        return
                    }
                    val searchPosition =
                        if (rvAdapter.searchPosition < rvLayoutManager.findFirstVisibleItemPosition() ||
                            rvAdapter.searchPosition > rvLayoutManager.findLastVisibleItemPosition()
                        ) {
                            rvLayoutManager.findFirstVisibleItemPosition()
                        } else {
                            rvAdapter.searchPosition + 1
                        }
                    showLoading()
                    Thread {
                        for (i in searchPosition until logs[currentLogIndex].size) {
                            logs[currentLogIndex][i].apply {
                                if (contains(key)) {
                                    runOnUiThread {
                                        rvLog.scrollToPosition(i)
                                        rvAdapter.setSearchResult(key, i)
                                        showSuccess()
                                    }
                                    return@Thread
                                }
                            }
                        }
                        ToastUtils.showShort("往下没有了")
                        runOnUiThread {
                            if (key != rvAdapter.searchKey) {
                                rvAdapter.setSearchResult("", -1)
                            }
                            showSuccess()
                        }
                    }.start()
                }
            }
        }
    }
}