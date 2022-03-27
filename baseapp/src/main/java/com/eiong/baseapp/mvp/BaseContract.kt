package com.eiong.baseapp.mvp

import androidx.lifecycle.LifecycleOwner

/**
 * MVP
 *
 * @author EIong
 */
interface BaseContract {
    /**
     * MVP
     */
    interface BasePresenter {
        /**
         * 依附View
         *
         * @param view 依附的View
         */
        fun attachView(lifecycleOwner: LifecycleOwner, view: BaseView)

        /**
         * 分离View
         */
        fun detachView()

        /**
         * 取消所有操作
         */
        fun cancelAll() {}
    }

    /**
     * MVP
     */
    interface BaseView
}