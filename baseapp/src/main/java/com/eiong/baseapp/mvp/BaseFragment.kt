package com.eiong.baseapp.mvp

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ReflectUtils
import com.eiong.baseapp.mvc.BaseFragment
import java.lang.reflect.ParameterizedType

/**
 * MVP
 *
 * @author EIong
 *
 * @param VB 绑定视图
 * @param P  数据处理
 */
open class BaseFragment<VB : ViewBinding, P : BaseContract.BasePresenter> : BaseFragment<VB>(),
    BaseContract.BaseView {
    /** 数据处理 */
    protected val p: P by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val pClass = type.actualTypeArguments[1] as Class<*>
        ReflectUtils.reflect(pClass).newInstance().get()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        p.attachView(this, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        p.apply {
            cancelAll()
            detachView()
        }
    }
}