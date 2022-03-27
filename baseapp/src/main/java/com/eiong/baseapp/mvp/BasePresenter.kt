package com.eiong.baseapp.mvp

import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * MVP
 *
 * @author EIong
 *
 * @param V View
 */
open class BasePresenter<V : BaseContract.BaseView> : BaseContract.BasePresenter {
    /** View对象 */
    protected lateinit var v: V
    private lateinit var vWR: WeakReference<V>

    /** LifecycleOwner对象 */
    protected lateinit var lo: LifecycleOwner
    private lateinit var loWR: WeakReference<LifecycleOwner>

    override fun attachView(lifecycleOwner: LifecycleOwner, view: BaseContract.BaseView) {
        @Suppress("UNCHECKED_CAST")
        vWR = WeakReference(view as V)
        vWR.get()?.let { v = it }
        loWR = WeakReference(lifecycleOwner)
        loWR.get()?.let { lo = it }
    }

    override fun detachView() {
        vWR.clear()
        loWR.clear()
        System.gc()
    }
}