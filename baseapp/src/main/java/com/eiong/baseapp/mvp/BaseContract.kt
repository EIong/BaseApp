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
         * 依附视图
         *
         * @param view 依附的视图
         */
        fun attachView(lifecycleOwner: LifecycleOwner, view: BaseView)

        /**
         * 分离视图
         */
        fun detachView()

        /**
         * 取消所有
         */
        fun cancelAll() {}
    }

    /**
     * MVP
     */
    interface BaseView
}